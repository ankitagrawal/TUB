package com.hk.web.action.admin.courier;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class SearchOrderAndEnterCourierInfoAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(SearchOrderAndEnterCourierInfoAction.class);

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    ShippingOrder shippingOrder;
    @Autowired
    ShippingOrderDao shippingOrderDao;
    @Autowired
    ShipmentService shipmentService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    UserService userService;
    @Autowired
    PincodeDao pincodeDao;
    @Autowired
    CourierGroupService courierGroupService;
    @Autowired
    private ShipmentPricingEngine shipmentPricingEngine;
    @Autowired
    AwbService awbService;
    
    @Autowired
    CourierServiceInfoDao courierServiceInfoDao;

    private String trackingId;
    private String gatewayOrderId;
    Courier suggestedCourier;
    List<Courier> availableCouriers;
    Double approxWeight = 0D;
    boolean isGroundShipped = false;

    Shipment shipment;

    @Autowired
    private CourierService courierService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;

    @ValidationMethod(on = "saveShipmentDetails")
    public void verifyShipmentDetails() {
        if (StringUtils.isBlank(trackingId) || shipment.getBoxWeight() == null || shipment.getBoxSize() == null || shipment.getCourier() == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Tracking Id, Box weight, Box Size, Courier all are mandatory"));
        }
       if ((shipment.getBoxSize() != null && shipment.getBoxSize().getId().equals(EnumBoxSize.MIGRATE.getId())) || (shipment.getCourier() != null && shipment.getCourier().getId().equals(EnumCourier.MIGRATE.getId()))) {
            getContext().getValidationErrors().add("2", new SimpleError("None of the values can be migrate"));
        }
        Pincode pinCode = pincodeDao.getByPincode(shippingOrder.getBaseOrder().getAddress().getPin());
        if (pinCode == null) {
            getContext().getValidationErrors().add("3", new SimpleError("Pincode is invalid, It cannot be packed"));
        } else {
            boolean isCod = shippingOrder.isCOD();
            
//  groundShipping logic Starts---
        isGroundShipped =  shipmentService.isShippingOrderHasGroundShippedItem(shippingOrder);
        availableCouriers = courierService.getAvailableCouriers(pinCode.getPincode(), isCod, isGroundShipped, false,false);
//  ground shipping logic ends

            if (availableCouriers == null || availableCouriers.isEmpty()) {
                getContext().getValidationErrors().add("4", new SimpleError("No Couriers are applicable on this pincode, Please contact logistics, Order cannot be packed"));
            }
	        if (suggestedCourier != null) {
		        if ((availableCouriers != null && availableCouriers.size() > 0) && !(availableCouriers.contains(suggestedCourier))) {
			        getContext().getValidationErrors().add("5", new SimpleError(" ERROR :::: The Default suggessted courier " + suggestedCourier.getName() + " " +
					        "is not present in Servicable Courier (Available List).       Contact Admin(Rajinder) To add " + suggestedCourier.getName() + " in servicable List  for Pincode " + pinCode.getPincode()));
		        }
	        }

        }
    }

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_PACKING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {

        return new ForwardResolution("/pages/admin/searchOrderAndEnterCouierInfo.jsp");
    }

    @DontValidate
    public Resolution searchOrders() {
        shippingOrder = shippingOrderDao.findByGatewayOrderId(gatewayOrderId);
        if (shippingOrder == null) {
            addRedirectAlertMessage(new SimpleMessage("Shipping Order not found for the corresponding gateway order id"));
            return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
        } else {
            if (EnumShippingOrderStatus.getStatusForSearchOrderAndEnterCourierInfo().contains(shippingOrder.getOrderStatus().getId())) {
                shipment = shippingOrder.getShipment();
                shippingOrderList.add(shippingOrder);
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    if (lineItem.getSku().getProductVariant().getWeight() != null) {
                        approxWeight += lineItem.getSku().getProductVariant().getWeight();
                    }
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("Shipping Order is not checked out. It cannot be packed. "));
                return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
            }
        }

        try {
            Pincode pinCode = pincodeDao.getByPincode(shippingOrder.getBaseOrder().getAddress().getPin());
            if (pinCode != null) {
                boolean isCod = shippingOrder.isCOD();
                isGroundShipped = shipmentService.isShippingOrderHasGroundShippedItem(shippingOrder);
                availableCouriers = courierService.getAvailableCouriers(pinCode.getPincode(), isCod, isGroundShipped, false,false);
                if (shippingOrder.getShipment() != null && shippingOrder.getShipment().getCourier() != null && shippingOrder.getShipment().getAwb() != null && shippingOrder.getShipment().getAwb().getAwbNumber() != null) {
                    suggestedCourier = shippingOrder.getShipment().getCourier();
                    trackingId = shippingOrder.getShipment().getAwb().getAwbNumber();
                } else {
                     suggestedCourier = courierService.getDefaultCourierByPincodeForLoggedInWarehouse(pinCode, isCod, isGroundShipped);                    
                    //Todo: Seema ."reason=create  shipment with default Awb  " Action: default Tracking id= gateway_order_id: Might remove when we have all the awb in system
                    trackingId = shippingOrder.getGatewayOrderId();
                }
	            if (suggestedCourier != null) {
		            if ((availableCouriers != null && availableCouriers.size() > 0) && (!(availableCouriers.contains(suggestedCourier)))) {
			            addRedirectAlertMessage(new SimpleMessage("The Default suggessted courier " + suggestedCourier.getName() + " is not present in Servicable Courier (Available List)" +
					            "       Contact Admin(Rajinder) To add Servicable List for Pincode " + pinCode.getPincode()));
		            }
	            }

            } else {
                addRedirectAlertMessage(new SimpleMessage("Pincode is INVALID, Please contact Customer Care. It cannot be packed."));
            }

        } catch (Exception e) {
            logger.error("Error while getting suggested courier for shippingOrder#" + shippingOrder.getId(), e);
        }
        return new ForwardResolution("/pages/admin/searchOrderAndEnterCouierInfo.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PACKING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution saveShipmentDetails() {
        shipment.setEmailSent(false);
        if (trackingId == null) {
            addRedirectAlertMessage(new SimpleMessage("Pincode is INVALID, Please contact Customer Care. It cannot be packed."));
            return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
        }
        Awb finalAwb = null;
        Awb suggestedAwb = null;
        if (shippingOrder.getShipment() != null) {
            suggestedAwb = shippingOrder.getShipment().getAwb();
        }
        finalAwb = suggestedAwb;
        if ((suggestedAwb == null) || (!(suggestedAwb.getAwbNumber().equalsIgnoreCase(trackingId.trim()))) ||
                (suggestedCourier != null && (!(shipment.getCourier().equals(suggestedCourier))))) {

            if ((suggestedAwb != null) && (suggestedCourier != null) && (ThirdPartyAwbService.integratedCouriers.contains(suggestedCourier.getId()))){
                // To delete the tracking no. generated previously
                awbService.deleteAwbForThirdPartyCourier(suggestedCourier, suggestedAwb.getAwbNumber());
            }

            if (ThirdPartyAwbService.integratedCouriers.contains(shipment.getCourier().getId())) {
               Double weightInKg = shipment.getBoxWeight();
               Awb thirdPartyAwb = awbService.getAwbForThirdPartyCourier(shipment.getCourier(), shippingOrder, weightInKg);
               if (thirdPartyAwb == null) {
                    addRedirectAlertMessage(new SimpleMessage(" The tracking number could not be generated"));
                    return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
               } else {
                   thirdPartyAwb = awbService.save(thirdPartyAwb);
                   finalAwb = thirdPartyAwb;
                   finalAwb.setAwbStatus(EnumAwbStatus.Attach.getAsAwbStatus());
               }
            }           
            else {
                Awb awbFromDb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(shipment.getCourier(), trackingId.trim(), null, null, null);
                if (awbFromDb != null && awbFromDb.getAwbNumber() != null) {
                    if (awbFromDb.getAwbStatus().getId().equals(EnumAwbStatus.Used.getId()) || (awbFromDb.getAwbStatus().getId().equals(EnumAwbStatus.Attach.getId())) || (awbFromDb.getAwbStatus().getId().equals(EnumAwbStatus.Authorization_Pending.getId()))) {
                        addRedirectAlertMessage(new SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "is already Used with other  shipping Order"));
                        return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
                    }
                    if ((!awbFromDb.getWarehouse().getId().equals(shippingOrder.getWarehouse().getId())) || (awbFromDb.getCod() != shippingOrder.isCOD())) {
                        addRedirectAlertMessage(new SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "is already Present in another warehouse with same courier" +
                                "  : " + shipment.getCourier().getName() + "  you are Trying to use COD tracking id with NON COD   TRY AGAIN "));
                        return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
                    }

                    finalAwb = awbFromDb;
                    finalAwb.setAwbStatus(EnumAwbStatus.Attach.getAsAwbStatus());
                } else {
                    //TODO:#change this all logic should be at one place in awbService
                    Awb awb = new Awb();
                    awb.setAwbNumber(trackingId.trim());
                    awb.setAwbBarCode(trackingId.trim());
                    awb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
                    awb.setCourier(shipment.getCourier());
                    awb.setCod(shippingOrder.isCOD());
                    awb.setWarehouse(shippingOrder.getWarehouse());
                    awb = awbService.save(awb);
                    finalAwb = awb;
                    finalAwb.setAwbStatus(EnumAwbStatus.Authorization_Pending.getAsAwbStatus());     //new awb taken according to trackingId manually entered, as DB has none
                }
                //Todo: Seema --  Awb which are detached from Shipment,their status should not change:Need to check if awb should be deleted or made free for reuse
                /*if (suggestedAwb != null) {
                    suggestedAwb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
                    awbService.save(suggestedAwb);
                }*/
            }
        } else {
            finalAwb.setAwbStatus(EnumAwbStatus.Attach.getAsAwbStatus());  //comes here when no change at all
        }


        shipment.setAwb(finalAwb);
        shipment.setShippingOrder(shippingOrder);
        shippingOrder.setShipment(shipment);
        if (courierGroupService.getCourierGroup(shipment.getCourier()) != null) {
            shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
            shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
            shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
        }
        shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Packed));
        shippingOrderDao.save(shippingOrder);
        String comment = "";
        if (shipment != null) {
            String trackingId = shipment.getAwb().getAwbNumber();
            comment = "Shipment Details: " + shipment.getCourier().getName() + "/" + trackingId;
        }
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Packed, comment);

        return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
    }

    public List<ShippingOrder> getShippingOrderList() {
        return shippingOrderList;
    }

    public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
        this.shippingOrderList = shippingOrderList;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }


    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public Courier getSuggestedCourier() {
        return suggestedCourier;
    }

    public void setSuggestedCourier(Courier suggestedCourier) {
        this.suggestedCourier = suggestedCourier;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public List<Courier> getAvailableCouriers() {
        return availableCouriers;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }

    public Double getApproxWeight() {
        return approxWeight;
    }

    public void setApproxWeight(Double approxWeight) {
        this.approxWeight = approxWeight;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public boolean isGroundShipped() {
        return isGroundShipped;
    }

    public void setGroundShipped(boolean groundShipped) {
        isGroundShipped = groundShipped;
    }
}