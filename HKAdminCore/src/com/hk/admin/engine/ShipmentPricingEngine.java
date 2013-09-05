package com.hk.admin.engine;

import com.hk.admin.pact.service.courier.CourierCostCalculator;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.core.EnumTax;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumCourierOperations;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.Shipment;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Payment;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
  ShipmentService shipmentService;

  @Autowired
  CourierService courierService;

  public Double calculateShipmentCost(ShippingOrder shippingOrder){
    Shipment shipment = shippingOrder.getShipment();
    Courier courier = shipment.getAwb().getCourier();
    Double weight = shipment.getBoxWeight() * 1000;
    EnumBoxSize enumBoxSize = EnumBoxSize.getBoxSize(shipment.getBoxSize());
    // weight remains as physical weight if HK delivery or courier with physical weight restriction
    if(!EnumCourier.HK_Delivery.getId().equals(courier.getId())
        || !(courier.getOperationsBitset() % EnumCourierOperations.PHYSICAL_WT_PREFERRED.getId() == 0)){
      if (enumBoxSize != null) {
        if (enumBoxSize.getVolumetricWeight() > weight) {
          weight = enumBoxSize.getVolumetricWeight();
        }
      }
    }
    Order order = shippingOrder.getBaseOrder();
    Pincode pincodeObj = order.getAddress().getPincode();
    Warehouse srcWarehouse = shippingOrder.getWarehouse();
    if (EnumCourier.HK_Delivery.getId().equals(courier.getId())) {
      if (pincodeObj.getNearestHub() != null) {
        HKReachPricingEngine hkReachPricingEngine = courierService.getHkReachPricingEngine(srcWarehouse, pincodeObj.getNearestHub());
        if(hkReachPricingEngine != null){
          return calculateHKReachCost(hkReachPricingEngine, weight, pincodeObj);
        }
      }
    } else {
      CourierPricingEngine courierPricingInfo = courierCostCalculator.getCourierPricingInfo(courier, pincodeObj, srcWarehouse);
      if (courierPricingInfo != null) {
        return calculateShipmentCost(courierPricingInfo, weight);
      } else {
        return null;
      }
    }
    return 0D;
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

  public Double calculateHKReachCost(HKReachPricingEngine hkReachPricingEngine, Double weight, Pincode pincode) {
    return (hkReachPricingEngine.getInterCityCost() + hkReachPricingEngine.getFixedCost()) * weight / 1000 + pincode.getLastMileCost();
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

    if (EnumCourier.HK_Delivery.getId().equals(courier.getId()))
      return this.calculateReconcillationCostHKReach(shippingOrder.getBaseOrder().getPayment(), shippingOrder.getAmount());

    CourierPricingEngine courierPricingInfo = courierCostCalculator.getCourierPricingInfo(courier, pincodeObj, srcWarehouse);
    if (courierPricingInfo == null) {
      return null;
    }
    return calculateReconciliationCost(courierPricingInfo, shippingOrder.getBaseOrder().getPayment(), shippingOrder.getAmount());
  }

  public Double calculatePackagingCost(ShippingOrder shippingOrder) {
    Shipment shipment = shippingOrder.getShipment();
    EnumBoxSize enumBoxSize = EnumBoxSize.getBoxSize(shipment.getBoxSize());
    return enumBoxSize != null && enumBoxSize.getId() != -1 ? enumBoxSize.getPackagingCost() : 15.5D;
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
    if (courierPricingEngine != null) {
      if (cod) {
        reconciliationCharges = amount > courierPricingEngine.getCodCutoffAmount()
            ? amount * courierPricingEngine.getVariableCodCharges() : courierPricingEngine.getMinCodCharges();
        reconciliationCharges = reconciliationCharges * (1 + EnumTax.VAT_12_36.getValue());
      } else {
        reconciliationCharges = amount * 0.022;
      }
    }
    return reconciliationCharges;
  }

  private Double calculateReconcillationCostHKReach(Payment payment, Double amount) {
    Double reconciliationCharges = 0d;
    GatewayIssuerMapping gatewayIssuerMapping = gatewayIssuerMappingService.getGatewayIssuerMapping(payment.getIssuer(),
        payment.getGateway(), null);
    if (gatewayIssuerMapping != null) {
      reconciliationCharges = amount * gatewayIssuerMapping.getReconciliationCharge();
      reconciliationCharges = reconciliationCharges * (1 + EnumTax.VAT_12_36.getValue());
    }
    return reconciliationCharges;
  }
}
