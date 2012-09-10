package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    CourierService courierService;
    @Autowired
    PincodeDao pincodeDao;
    @Autowired
    AwbDao awbDao;
    @Autowired
    CourierGroupService courierGroupService;
    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;
    @Autowired
    AwbService awbService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    ShipmentDao shipmentDao;

    public Shipment createShipment(ShippingOrder shippingOrder) {
        Order order = shippingOrder.getBaseOrder();
        Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
        if (pincode == null) {
            return null;
        }
        // Ground Shipping logic starts -- suggested courier
        boolean isGroundShipped = false;
        Courier suggestedCourier = null;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            if (lineItem.getSku().getProductVariant().getProduct().isGroundShipping()) {
                isGroundShipped = true;
                break;
            }
        }
        if (isGroundShipped) {
            suggestedCourier = courierService.getDefaultCourier(pincode, shippingOrder.isCOD(), isGroundShipped, shippingOrder.getWarehouse());
        } else {
            suggestedCourier = courierService.getDefaultCourier(pincode, shippingOrder.isCOD(), false, shippingOrder.getWarehouse());
        }
//      Ground Shipping logic ends -- suggested courier  
        if (suggestedCourier == null) {
            return null;
        }
        Awb suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(suggestedCourier, null, shippingOrder.getWarehouse(), shippingOrder.isCOD(), EnumAwbStatus.Unused.getAsAwbStatus());
        if (suggestedAwb == null) {
            return null;
        }
        Double estimatedWeight = 100D;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            if (lineItem.getSku().getProductVariant().getProduct().isDropShipping()) {
                return null;
            }
            Double variantWeight = productVariant.getWeight();
            if (variantWeight == null || variantWeight == 0D) {
                estimatedWeight += 0D;
            } else {
                estimatedWeight += variantWeight;
            }
        }
        Shipment shipment = new Shipment();
        shipment.setCourier(suggestedCourier);
        shipment.setEmailSent(false);
        suggestedAwb.setUsed(true);
        suggestedAwb.setAwbStatus(EnumAwbStatus.Attach.getAsAwbStatus());
        suggestedAwb = awbService.save(suggestedAwb);
        shipment.setAwb(suggestedAwb);
        shipment.setShippingOrder(shippingOrder);
        shipment.setBoxWeight(estimatedWeight / 1000);
        shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
        shippingOrder.setShipment(shipment);
        if (courierGroupService.getCourierGroup(shipment.getCourier()) != null) {
            shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
            shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
            shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
        }
        shippingOrder = shippingOrderService.save(shippingOrder);
        String trackingId = shipment.getAwb().getAwbNumber();
        String comment = "Shipment Details: " + shipment.getCourier().getName() + "/" + trackingId;
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created, comment);
        return shippingOrder.getShipment();
    }

    public Shipment saveShipmentDate(Shipment shipment) {
        shipment.setShipDate(new Date());
        return save(shipment);
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) shipmentDao.save(shipment);
    }

    public Awb attachAwbToShipment(Courier courier, ShippingOrder shippingOrder) {
        Shipment shipment = shippingOrder.getShipment();
        Awb suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), shippingOrder.isCOD(), EnumAwbStatus.Unused.getAsAwbStatus());
        if (suggestedAwb != null) {
            AwbStatus awbStatus = EnumAwbStatus.Attach.getAsAwbStatus();
            suggestedAwb.setAwbStatus(awbStatus);
            shipment.setAwb(suggestedAwb);
            return suggestedAwb;
        }
        return null;
    }

    public Shipment findByAwb(Awb awb) {
        return shipmentDao.findByAwb(awb);
    }

    public void delete(Shipment shipment) {
        shipmentDao.delete(shipment);
    }
}
