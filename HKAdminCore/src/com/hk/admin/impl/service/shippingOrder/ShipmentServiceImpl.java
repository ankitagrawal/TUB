package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.courier.AwbDao;
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
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.ShipmentServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@SuppressWarnings("NullableProblems")
@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    CourierService courierService;
    @Autowired
    PincodeCourierService pincodeCourierService;
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
    @Autowired
    UserService userService;
    @Autowired
    AdminEmailManager adminEmailManager;

    @Transactional
    public Shipment createShipment(ShippingOrder shippingOrder) {
        if (shippingOrder.getShipment() == null) {

            Order order = shippingOrder.getBaseOrder();
            User adminUser = getUserService().getAdminUser();
            Pincode pincode = order.getAddress().getPincode();

            ShipmentServiceType shipmentServiceType = pincodeCourierService.getShipmentServiceType(shippingOrder);
            boolean isCod = ShipmentServiceMapper.isCod(shipmentServiceType);
            boolean isGround = ShipmentServiceMapper.isGround(shipmentServiceType);

            Courier suggestedCourier = courierService.getDefaultCourier(pincode, isCod, isGround, shippingOrder.getWarehouse());
            Zone zone = pincode.getZone();

/*
        if(shippingOrder instanceof ReplacementOrder){
            ShippingOrder parentShippingOrder = ((ReplacementOrder) shippingOrder).getRefShippingOrder();
            if(((ReplacementOrder) shippingOrder).isRto()){
                ReplacementOrderReason replacementOrderReason = getAdminShippingOrderService().getRTOReasonForShippingOrder(parentShippingOrder);
                if(replacementOrderReason != null){
                    if(EnumReplacementOrderReason.getCourierRelatedReasonForRto().contains(replacementOrderReason.getId())){
                        if(suggestedCourier != null && parentShippingOrder.getShipment().getAwb().getCourier().getId().equals(suggestedCourier.getId())){
                        }
                    }
                }
            }
*/

            if (suggestedCourier == null) {
                shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                        CourierConstants.SUGGESTED_COURIER_NOT_FOUND);
                adminEmailManager.sendNoShipmentEmail(CourierConstants.SUGGESTED_COURIER_NOT_FOUND, shippingOrder, shippingOrder.getBaseOrder());
                return null;
            } else {
                if (!pincodeCourierService.isCourierAvailable(pincode, null, Arrays.asList(shipmentServiceType), true)) {
                    shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_LoggedComment.asShippingOrderLifecycleActivity(),
                            CourierConstants.COURIER_SERVICE_INFO_NOT_FOUND);
                    adminEmailManager.sendNoShipmentEmail(CourierConstants.COURIER_SERVICE_INFO_NOT_FOUND, shippingOrder, shippingOrder.getBaseOrder());
                }
            }

            for (LineItem lineItem : shippingOrder.getLineItems()) {
                if (lineItem.getSku().getProductVariant().getProduct().isDropShipping()) {
                    shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser,
                            EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(), CourierConstants.DROP_SHIPPED_ORDER);
                    return null;
                }
            }

            Double weightInKg = getEstimatedWeightOfShipment(shippingOrder);
            Awb suggestedAwb = attachAwbToShipment(suggestedCourier, shippingOrder, weightInKg);

            // If we dont have AWB , shipment will not be created
            if (suggestedAwb == null) {
                String msg = CourierConstants.AWB_NOT_ASSIGNED + suggestedCourier.getName();
                shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                        msg);
                if (!(ThirdPartyAwbService.integratedCouriers.contains(suggestedCourier.getId()))) {
                    adminEmailManager.sendAwbStatusEmail(suggestedCourier, shippingOrder);
                }
                return null;
            }


            Shipment shipment = new Shipment();
            shipment.setEmailSent(false);
            shipment.setAwb(suggestedAwb);
            shipment.setShippingOrder(shippingOrder);
            shipment.setBoxWeight(weightInKg);
            shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
            shipment.setShipmentServiceType(shipmentServiceType);
            shippingOrder.setShipment(shipment);
            shipment.setZone(zone);
            if (courierGroupService.getCourierGroup(shipment.getAwb().getCourier()) != null) {
                shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
                shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
                shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
            }
            shippingOrder = shippingOrderService.save(shippingOrder);
            String trackingId = shipment.getAwb().getAwbNumber();
            String comment = shipmentServiceType.getName() + CourierConstants.SHIPMENT_DETAILS + shipment.getAwb().getCourier().getName() + "/" + trackingId;

            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created.asShippingOrderLifecycleActivity(),
                    comment);
        }
        return shippingOrder.getShipment();
    }

    public Shipment saveShipmentDate(Shipment shipment) {
        shipment.setShipDate(new Date());
        return save(shipment);
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) shipmentDao.save(shipment);
    }

    @Transactional
    private Awb attachAwbToShipment(Courier suggestedCourier, ShippingOrder shippingOrder, Double weightInKg) {
        ShipmentServiceType shipmentServiceType = pincodeCourierService.getShipmentServiceType(shippingOrder);
        boolean isCod = ShipmentServiceMapper.isCod(shipmentServiceType);
        Awb suggestedAwb;
        if (ThirdPartyAwbService.integratedCouriers.contains(suggestedCourier.getId())) {
            suggestedAwb = awbService.getAwbForThirdPartyCourier(suggestedCourier, shippingOrder, weightInKg);
            if (suggestedAwb != null) {
                suggestedAwb = (Awb) awbService.save(suggestedAwb, null);
                awbService.save(suggestedAwb, EnumAwbStatus.Attach.getId().intValue());
            }
        } else {
            suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(suggestedCourier, null, shippingOrder.getWarehouse(), isCod, EnumAwbStatus.Unused.getAsAwbStatus());
        }
        if (suggestedAwb == null) {
            return null;
        }
        int rowsUpdate = (Integer) awbService.save(suggestedAwb, EnumAwbStatus.Attach.getId().intValue());
        awbService.refresh(suggestedAwb);
        if (rowsUpdate == 1) {
            return suggestedAwb;
        } else {
            return attachAwbToShipment(suggestedCourier, shippingOrder, weightInKg);
        }
    }

    public Shipment findByAwb(Awb awb) {
        return shipmentDao.findByAwb(awb);
    }

    public void delete(Shipment shipment) {
        shipmentDao.delete(shipment);
    }

    @Override
    public Shipment changeCourier(Shipment shipment, Courier newCourier, boolean preserveAwb) {
        ShippingOrder shippingOrder = shipment.getShippingOrder();
        Awb currentAwb = shipment.getAwb();
        Awb suggestedAwb = attachAwbToShipment(newCourier, shippingOrder, shipment.getBoxWeight());
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
            awbService.save(awb, EnumAwbStatus.Used.getId().intValue());
        }
    }

    public Shipment recreateShipment(ShippingOrder shippingOrder) {
        Shipment newShipment = null;
        if (shippingOrder.getShipment() != null) {
            Shipment oldShipment = shippingOrder.getShipment();
            awbService.preserveAwb(oldShipment.getAwb());
            newShipment = createShipment(shippingOrder);
            shippingOrder.setShipment(newShipment);
            delete(oldShipment);
        }
        return newShipment;
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
