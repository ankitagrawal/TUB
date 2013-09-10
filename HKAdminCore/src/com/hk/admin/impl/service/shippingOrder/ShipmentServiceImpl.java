package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.engine.ShipmentCostDistributor;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.constants.analytics.EnumReason;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.*;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.Classification;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.WHReportLineItem;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.util.ShipmentServiceMapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    ShipmentDao shipmentDao;
    @Autowired
    UserService userService;
    @Autowired
    AdminEmailManager adminEmailManager;
    private static Logger logger = LoggerFactory.getLogger(ShipmentServiceImpl.class);


    public Shipment validateShipment(ShippingOrder shippingOrder) {
        Shipment validShipment = null;
        if (shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShipmentResolution()).contains(shippingOrder.getOrderStatus())) {
            User adminUser = getUserService().getAdminUser();
            Pincode pincode = shippingOrder.getBaseOrder().getAddress().getPincode();
            Zone zone = pincode.getZone();

            List<Reason> reasons = new ArrayList<Reason>();

            ShipmentServiceType shipmentServiceType = pincodeCourierService.getShipmentServiceType(shippingOrder);

            Courier suggestedCourier = pincodeCourierService.getDefaultCourier(shippingOrder);
            Awb suggestedAwb = null;
            Double weight = 0D;
            if (suggestedCourier == null) {
                reasons.add(EnumReason.SUGGESTED_COURIER_NOT_FOUND.asReason());
            } else {
                //todo courier need to handle replacement order ka rto courier
                if (!pincodeCourierService.isCourierAvailable(pincode, null, Arrays.asList(shipmentServiceType), true)) {
                    reasons.add(EnumReason.COURIER_SERVICE_INFO_NOT_FOUND.asReason());
                } else {
                    weight = getEstimatedWeightOfShipment(shippingOrder);
                    suggestedAwb = attachAwbForShipment(suggestedCourier, shippingOrder, weight);
                    if (suggestedAwb == null) {
                        reasons.add(EnumReason.AWB_NOT_ASSIGNED.asReason());
                    }
                }
            }

            for (Reason reason : reasons) {
                shippingOrderService.logShippingOrderActivityByAdmin(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated, reason);
                String reasoning = "";
                if(reason != null){
                    Classification classification = reason.getClassification();
                    if(classification != null){
                       reasoning = classification.getPrimary();
                    }
                }
                adminEmailManager.sendNoShipmentEmail(reasoning, shippingOrder, shippingOrder.getBaseOrder());
            }
            if (reasons.isEmpty()) {
                validShipment = new Shipment();
                validShipment.setAwb(suggestedAwb);
                validShipment.setBoxWeight(weight);
                validShipment.setShipmentServiceType(shipmentServiceType);
                validShipment.setZone(zone);
            }
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
                Double estimatedShipmentCharge = shipmentPricingEngine.calculateShipmentCost(shippingOrder);
                shipment.setOrderPlacedShipmentCharge(estimatedShipmentCharge);
                calculateAndDistributeShipmentCost(shipment);
            }
            shippingOrder = shippingOrderService.save(shippingOrder);
            String comment = shipment.getShipmentServiceType().getName() + shipment.getAwb().toString();
            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created.asShippingOrderLifecycleActivity(),
                    null, comment);
        }
        this.shipmentDao.save(shippingOrder);
        return shippingOrder.getShipment();
    }

    public Shipment calculateAndDistributeShipmentCost(Shipment shipment) {
        ShippingOrder shippingOrder = shipment.getShippingOrder();
        shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
        shipment.setShipmentCostCalculateDate(new Date());
        shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
        shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
        List<WHReportLineItem> whReportLineItemList = ShipmentCostDistributor.distributeShippingCost(shippingOrder);
        for (WHReportLineItem whReportLineItem : whReportLineItemList) {
            logger.debug("Line Item" + whReportLineItem.getLineItem() + " shipment charge "
                    + whReportLineItem.getEstmShipmentCharge() + " collection charge "
                    + whReportLineItem.getEstmCollectionCharge() + " extra charge " + whReportLineItem.getExtraCharge());
            shipmentDao.save(whReportLineItem);
        }
        return shipment;
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
            if(suggestedAwb != null){
                suggestedAwb = awbService.save(suggestedAwb,EnumAwbStatus.Unused.getId().intValue());
            }
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
    @Override
    public Shipment changeAwb(Shipment shipment,Awb newAwb,boolean preserveAwb){
        Awb currentAwb=shipment.getAwb();
        shipment.setAwb(newAwb);
        shipment = save(shipment);
        changeAwbStatus(currentAwb,preserveAwb);
      return shipment;
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
            String oldShipmentAwbDetails = oldShipment.getAwb().toString();
            shippingOrder.setShipment(null);
            delete(oldShipment);
            shippingOrder = shippingOrderService.save(shippingOrder);
            awbService.preserveAwb(oldShipment.getAwb());
            newShipment = createShipment(shippingOrder, true);
            shippingOrder.setShipment(newShipment);
            shippingOrder = shippingOrderService.save(shippingOrder);
            String newShipmentAwbDetails = "";
            if (newShipment != null && newShipment.getAwb() != null)
              newShipmentAwbDetails = newShipment.getAwb().toString();
            shippingOrderService.logShippingOrderActivity(shippingOrder, userService.getLoggedInUser(), EnumShippingOrderLifecycleActivity.SO_Shipment_Re_Created.asShippingOrderLifecycleActivity(),
                    null, oldShipmentAwbDetails + " to --> " + newShipmentAwbDetails);
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
        Double estimatedWeight = 180D;
        Double estimatedVolumetricWeight = 0D;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            Double variantWeight = productVariant.getWeight();
            if (variantWeight == null || variantWeight == 0D) {
                estimatedWeight += 0D;
            } else {
                estimatedWeight += variantWeight;
            }
            BoxSize estimatedBoxSize = productVariant.getEstimatedBoxSize();
            if(estimatedBoxSize != null){
                Double variantVolumetricWeight = estimatedBoxSize.getVolumetricWeight();
                if(estimatedVolumetricWeight < variantVolumetricWeight){
                    estimatedVolumetricWeight = variantVolumetricWeight;
                }
            }
        }
        Double maxWeight = estimatedWeight > estimatedVolumetricWeight ? estimatedWeight : estimatedVolumetricWeight;
        return maxWeight / 1000;
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
