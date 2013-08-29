package com.hk.admin.engine;

import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.service.courier.CourierCostCalculator;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.constants.core.EnumTax;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumCourierGroup;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@Component
public class ShipmentPricingEngine {

  private static Logger logger = LoggerFactory.getLogger(ShipmentPricingEngine.class);

  @Autowired
  PincodeDao pincodeDao;

  @Autowired
  CourierGroupService courierGroupService;

  @Autowired
  CourierCostCalculator courierCostCalculator;

  @Autowired
  GatewayIssuerMappingService gatewayIssuerMappingService;

  @Autowired
  private CourierPricingEngineDao courierPricingEngineDao;

  public Double calculateShipmentCost(ShippingOrder shippingOrder){
    Shipment shipment = shippingOrder.getShipment();
    Courier courier = shipment.getAwb().getCourier();
    Double weight = shipment.getBoxWeight() * 1000;

      EnumBoxSize enumBoxSize = EnumBoxSize.getBoxSize(shipment.getBoxSize());
      if (enumBoxSize != null) {
        if (enumBoxSize.getVolumetricWeight() > weight) {
          weight = enumBoxSize.getVolumetricWeight();
        }
      }

    Order order = shippingOrder.getBaseOrder();
    Pincode pincodeObj  = order.getAddress().getPincode();
    if(pincodeObj == null)   {
      return null;
    }
    Warehouse srcWarehouse = shippingOrder.getWarehouse();
    if (EnumCourier.HK_Delivery.getId().equals(courier.getId())) {
      return this.calculateHKReachCost(srcWarehouse,pincodeObj,weight);
    } else {
      CourierPricingEngine courierPricingInfo = courierCostCalculator.getCourierPricingInfo(courier, pincodeObj, srcWarehouse);
      if (courierPricingInfo != null) {
        return calculateShipmentCost(courierPricingInfo, weight);
      } else {
        return null;
      }
    }
  }

  public Double calculateShipmentCost(CourierPricingEngine courierPricingEngine, Double weight) {
    if(courierPricingEngine == null){
      return 0D;
    }
    Double additionalWeight = weight - (courierPricingEngine.getFirstBaseWt() + courierPricingEngine.getSecondBaseWt());
    Double remainder = 0D;

    if (additionalWeight > 0) {
      remainder = additionalWeight % courierPricingEngine.getAdditionalWt();
    }

    Double baseCost = courierPricingEngine.getFirstBaseCost();
    if (weight > courierPricingEngine.getFirstBaseWt()) {
      baseCost += courierPricingEngine.getSecondBaseCost();
    }

    int slabs = (int) (additionalWeight / courierPricingEngine.getAdditionalWt());

    if (remainder > 0) slabs = slabs + 1;
    Double additionalCost = additionalWeight > 0D ? slabs * courierPricingEngine.getAdditionalCost() : 0D;
    return (baseCost + additionalCost) * (1 + courierPricingEngine.getFuelSurcharge()) * (1 + EnumTax.VAT_12_36.getValue());
  }

  public Double calculateReconciliationCost(ShippingOrder shippingOrder){
    Shipment shipment = shippingOrder.getShipment();
    Order order = shippingOrder.getBaseOrder();
    Courier courier = shipment.getAwb().getCourier();
    Pincode pincodeObj = order.getAddress().getPincode();
    if(pincodeObj == null)   {
      return null;
    }
    Warehouse srcWarehouse = shippingOrder.getWarehouse();
    if(courier == null || courier.getId().equals(EnumCourier.MIGRATE.getId())) {
      logger.info("Courier is null for BO order " + order.getId());
      return null;
    }
    if(EnumCourier.HK_Delivery.asCourier().equals(courier)) {
      return calculateReconciliationCostForHKReach(shippingOrder);
    } else {
      CourierPricingEngine courierPricingInfo = courierCostCalculator.getCourierPricingInfo(courier, pincodeObj, srcWarehouse);
      if(courierPricingInfo == null)   {
        return null;
      } else {
        return calculateReconciliationCost(courierPricingInfo, shippingOrder);
      }
    }
  }

  public Double calculatePackagingCost(ShippingOrder shippingOrder) {
    Shipment shipment = shippingOrder.getShipment();
    EnumBoxSize enumBoxSize = EnumBoxSize.getBoxSize(shipment.getBoxSize());
    return enumBoxSize != null && enumBoxSize.getId() != -1 ? enumBoxSize.getPackagingCost() : 15.5D;
  }

  public Double calculateReconciliationCost(CourierPricingEngine courierPricingEngine, ShippingOrder shippingOrder) {
    return calculateReconciliationCost(courierPricingEngine, shippingOrder.getBaseOrder().getPayment(), shippingOrder.getAmount());
  }

  public Double calculateReconciliationCost(CourierPricingEngine courierPricingEngine, Payment payment, Double amount) {
    Double reconciliationCharges = 0D;
    if (payment != null) {
      if (payment.isCODPayment()) {
        reconciliationCharges = amount > courierPricingEngine.getCodCutoffAmount() ? amount * courierPricingEngine.getVariableCodCharges() : courierPricingEngine.getMinCodCharges();
        reconciliationCharges = reconciliationCharges * (1 + EnumTax.VAT_12_36.getValue());
      } else if (payment.getPaymentMode().getId().equals(EnumPaymentMode.ONLINE_PAYMENT.getId())) {
        GatewayIssuerMapping gatewayIssuerMapping = gatewayIssuerMappingService.getGatewayIssuerMapping(payment.getIssuer(), payment.getGateway(), null);
        if (gatewayIssuerMapping == null) {
          return 0D;
        }
        reconciliationCharges = amount * gatewayIssuerMapping.getReconciliationCharge();
        reconciliationCharges = reconciliationCharges * (1 + EnumTax.VAT_12_36.getValue());
      }
    }
    return reconciliationCharges;
  }

  public Double calculateReconciliationCost(CourierPricingEngine courierPricingEngine, Double amount, Boolean cod) {
    Double reconciliationCharges = 0D;
    if (cod) {
      reconciliationCharges = amount > courierPricingEngine.getCodCutoffAmount() ? amount * courierPricingEngine.getVariableCodCharges() : courierPricingEngine.getMinCodCharges();
      reconciliationCharges = reconciliationCharges * (1 + EnumTax.VAT_12_36.getValue());
    } else {
      reconciliationCharges = amount * 0.022;
    }
    return reconciliationCharges;
  }

  public Double calculateHKReachCost(Warehouse warehouse, Pincode destPincode, Double weightInGrams) {
    List<HKReachPricingEngine> pricingEngines = courierPricingEngineDao.getHkReachPricingEngine(warehouse,
                                                                          destPincode.getNearestHub(), false);
    HKReachPricingEngine pricingEngine = null;
    if (pricingEngines == null || destPincode.getNearestHub() == null) {
      return -1.0;
    } else {
      pricingEngine = pricingEngines.get(0);
    }

    Double shippingCost =  (pricingEngine.getInterCityCost() + pricingEngine.getFixedCost()) * weightInGrams/1000 +
                               destPincode.getLastMileCost();

    return shippingCost;
  }

  public Double calculateReconciliationCostForHKReach(ShippingOrder shippingOrder) {
    Double reconciliationCharges = 0D;
    Double amount = shippingOrder.getAmount();
    Payment payment = shippingOrder.getBaseOrder().getPayment();
    if (payment.getPaymentMode().getId().equals(EnumPaymentMode.ONLINE_PAYMENT.getId())) {
        GatewayIssuerMapping gatewayIssuerMapping = gatewayIssuerMappingService.getGatewayIssuerMapping(payment.getIssuer(), payment.getGateway(), null);
        if(gatewayIssuerMapping == null) {
            return 0D;
        }
        reconciliationCharges = amount * gatewayIssuerMapping.getReconciliationCharge();
        reconciliationCharges = reconciliationCharges * (1 + EnumTax.VAT_12_36.getValue());
    }
      return  reconciliationCharges;
    }
}
