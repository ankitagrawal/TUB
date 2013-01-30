package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.*;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.ShipmentServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("NullableProblems")
@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    CourierService courierService;
    @Autowired
    PincodeCourierService pincodeCourierService;
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
    @Autowired
    UserService userService;
    @Autowired
    AdminEmailManager adminEmailManager;

    public Shipment validateShipment(ShippingOrder shippingOrder) {
        User adminUser = getUserService().getAdminUser();
        Pincode pincode = shippingOrder.getBaseOrder().getAddress().getPincode();
        Zone zone = pincode.getZone();

        List<String> reasons = new ArrayList<String>();

        ShipmentServiceType shipmentServiceType = pincodeCourierService.getShipmentServiceType(shippingOrder);

        Courier suggestedCourier = pincodeCourierService.getDefaultCourier(shippingOrder);
        Awb suggestedAwb = null;
        Double weight = 0D;
        if (suggestedCourier == null) {
            reasons.add(CourierConstants.SUGGESTED_COURIER_NOT_FOUND);
        } else {
            //todo courier need to handle replacement order ka rto courier
            if (!pincodeCourierService.isCourierAvailable(pincode, null, Arrays.asList(shipmentServiceType), true)) {
                reasons.add(CourierConstants.COURIER_SERVICE_INFO_NOT_FOUND);
            } else {
                weight = getEstimatedWeightOfShipment(shippingOrder);
                suggestedAwb = attachAwbForShipment(suggestedCourier, shippingOrder, weight);
                if (suggestedAwb == null) {
                    reasons.add(CourierConstants.AWB_NOT_ASSIGNED);
                }
            }
        }

        for (String reason : reasons) {
            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                    reason);
            adminEmailManager.sendNoShipmentEmail(reason, shippingOrder, shippingOrder.getBaseOrder());
        }
        Shipment validShipment = null;
        if (reasons.isEmpty()) {
            validShipment = new Shipment();
            validShipment.setAwb(suggestedAwb);
            validShipment.setBoxWeight(weight);
            validShipment.setShipmentServiceType(shipmentServiceType);
            validShipment.setZone(zone);
        }
        return validShipment;
    }

    @Transactional
    public Shipment createShipment(ShippingOrder shippingOrder, Boolean validate) {
        User adminUser = getUserService().getAdminUser();
        Shipment shipment = shippingOrder.getShipment();
        if (validate) {
            shipment = validateShipment(shippingOrder);
        }
        if (shipment != null) {
            shipment.setEmailSent(false);
            shipment.setShippingOrder(shippingOrder);
            shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
            shippingOrder.setShipment(shipment);
            if (courierGroupService.getCourierGroup(shipment.getAwb().getCourier()) != null) {
                shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
                shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
                shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
            }
            shippingOrder = shippingOrderService.save(shippingOrder);
            String comment = shipment.getShipmentServiceType().getName() + shipment.getAwb().toString();
            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created.asShippingOrderLifecycleActivity(),
                    comment);
        }
        return shippingOrder.getShipment();
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) shipmentDao.save(shipment);
    }

    public void delete(Shipment shipment) {
        shipmentDao.delete(shipment);
    }

    public Shipment findByAwb(Awb awb) {
        return shipmentDao.findByAwb(awb);
    }

    @Transactional
    private Awb fetchAwbForShipment(Courier suggestedCourier, ShippingOrder shippingOrder, Double weightInKg) {
        ShipmentServiceType shipmentServiceType = pincodeCourierService.getShipmentServiceType(shippingOrder);
        boolean isCod = ShipmentServiceMapper.isCod(shipmentServiceType);
        Awb suggestedAwb;
        if (ThirdPartyAwbService.integratedCouriers.contains(suggestedCourier.getId())) {
            suggestedAwb = awbService.getAwbForThirdPartyCourier(suggestedCourier, shippingOrder, weightInKg);
        } else {
            suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(suggestedCourier, null, shippingOrder.getWarehouse(), isCod, EnumAwbStatus.Unused.getAsAwbStatus());
        }
        return suggestedAwb;
    }

    @Transactional
    private Awb attachAwbForShipment(Courier suggestedCourier, ShippingOrder shippingOrder, Double weightInKg) {
        Awb suggestedAwb = fetchAwbForShipment(suggestedCourier, shippingOrder, weightInKg);
        if (suggestedAwb != null) {
            suggestedAwb = awbService.save(suggestedAwb, EnumAwbStatus.Attach.getId().intValue());
            if (suggestedAwb == null) {
                return attachAwbForShipment(suggestedCourier, shippingOrder, weightInKg);
            }
        }
        return suggestedAwb;
    }

    @Override
    public Shipment changeCourier(Shipment shipment, Courier newCourier, boolean preserveAwb) {
        ShippingOrder shippingOrder = shipment.getShippingOrder();
        Awb currentAwb = shipment.getAwb();
        Awb suggestedAwb = attachAwbForShipment(newCourier, shippingOrder, shipment.getBoxWeight());
        if (suggestedAwb != null) {
            shipment.setAwb(suggestedAwb);
            shipment = save(shipment);
            changeAwbStatus(currentAwb, preserveAwb);
            return shipment;
        }
        return null;
    }

    public void changeAwbStatus(Awb awb, boolean preserveAwb) {
        if (preserveAwb) {
            awbService.preserveAwb(awb);
        } else {
            awbService.discardAwb(awb);
        }
    }

    public Shipment recreateShipment(ShippingOrder shippingOrder) {
        Shipment newShipment = null;
        if (shippingOrder.getShipment() != null) {
            Shipment oldShipment = shippingOrder.getShipment();
            shippingOrder.setShipment(null);
            delete(oldShipment);
            shippingOrder = shippingOrderService.save(shippingOrder);
            awbService.preserveAwb(oldShipment.getAwb());
            newShipment = createShipment(shippingOrder, true);
            shippingOrder.setShipment(newShipment);
            shippingOrder = shippingOrderService.save(shippingOrder);
        }
        return shippingOrder.getShipment();
    }

    @Override
    public boolean isValidShipment(Shipment shipment) {
        Awb awb = shipment.getAwb();
        //todo
        return true;
    }

    public Double getEstimatedWeightOfShipment(ShippingOrder shippingOrder) {
        Double estimatedWeight = 100D;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            Double variantWeight = productVariant.getWeight();
            if (variantWeight == null || variantWeight == 0D) {
                estimatedWeight += 0D;
            } else {
                estimatedWeight += variantWeight;
            }
        }
        return estimatedWeight / 1000;
    }

    public UserService getUserService() {
        return userService;
    }

    public boolean isShippingOrderHasInstallableItem(ShippingOrder shippingOrder) {
        if (shippingOrder.isDropShipping()) {
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                if (lineItem.getSku().getProductVariant().getProduct().getInstallable()) {
                    return true;
                }
            }
        }
        return false;
    }
}
