package com.hk.admin.engine;

import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.admin.pact.service.courier.CourierCostCalculator;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.constants.core.EnumTax;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumCourierGroup;
import com.hk.constants.payment.EnumPaymentMode;
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
import com.hk.pact.service.core.WarehouseService;
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
    CourierPricingEngineDao courierPricingEngineDao;

    @Autowired
    PincodeRegionZoneDao pincodeRegionZoneDao;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    CourierServiceInfoDao courierServiceInfoDao;

    @Autowired
    PincodeDao pincodeDao;

    @Autowired
    CourierGroupService courierGroupService;

    @Autowired
    CourierCostCalculator courierCostCalculator;

    public Double calculateShipmentCost(ShippingOrder shippingOrder){
        Shipment shipment = shippingOrder.getShipment();
        Courier courier = shipment.getCourier();
        Double weight = shipment.getBoxWeight() * 1000;
        if (EnumCourierGroup.COMMON.getId().equals(courierGroupService.getCourierGroup(courier).getId())) {
            EnumBoxSize enumBoxSize = EnumBoxSize.getBoxSize(shipment.getBoxSize());
            if (enumBoxSize != null) {
                if (enumBoxSize.getWeight() > weight) {
                    weight = enumBoxSize.getWeight();
                }
            }
        }
        Order order = shippingOrder.getBaseOrder();
        String pincode = order.getAddress().getPin();
        Pincode pincodeObj = pincodeDao.getByPincode(pincode);
        if(pincodeObj == null)   {
            logger.info("Illegal pincode " + pincode + "for BO order " + order.getId());
            return null;
        }
        Warehouse srcWarehouse = shippingOrder.getWarehouse();
        CourierPricingEngine courierPricingInfo = courierCostCalculator.getCourierPricingInfo(courier, pincodeObj, srcWarehouse);
        if(courierPricingInfo == null)   {
            return null;
        }
        return calculateShipmentCost(courierPricingInfo, weight);
    }

    public Double calculateShipmentCost(CourierPricingEngine courierPricingEngine, Double weight) {
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
        Courier courier = shipment.getCourier();
        String pincode = order.getAddress().getPin();
        Pincode pincodeObj = pincodeDao.getByPincode(pincode);
        if(pincodeObj == null)   {
            logger.info("Illegal pincode " + pincode + "for BO order " + order.getId());
            return null;
        }
        Warehouse srcWarehouse = shippingOrder.getWarehouse();
        if(courier == null || courier.getId().equals(EnumCourier.MIGRATE.getId())) {
            logger.info("Courier is null for BO order " + order.getId());
            return null;
        }
        CourierPricingEngine courierPricingInfo = courierCostCalculator.getCourierPricingInfo(courier, pincodeObj, srcWarehouse);
        if(courierPricingInfo == null)   {
            return null;
        }
        return calculateReconciliationCost(courierPricingInfo, shippingOrder);
    }

    public Double calculatePackagingCost(ShippingOrder shippingOrder) {
        Shipment shipment = shippingOrder.getShipment();
        EnumBoxSize enumBoxSize = EnumBoxSize.getBoxSize(shipment.getBoxSize());
        return enumBoxSize.getPackagingCost();
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
            } else if (payment.getPaymentMode().getId().equals(EnumPaymentMode.CITRUS.getId())) {   //todo for citrus credit/debit and ebs rates
                reconciliationCharges = amount * 0.017;
            } else if (payment.getPaymentMode().getId().equals(EnumPaymentMode.TECHPROCESS.getId())) {
                reconciliationCharges = amount * 0.0236;
            } else{
                reconciliationCharges = amount * 0.02;
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
            reconciliationCharges = amount * 0.0218;
        }
        return reconciliationCharges;
    }
}
