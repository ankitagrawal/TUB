package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
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
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.List;

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

    private String trackingId;
    private String gatewayOrderId;
    Courier suggestedCourier;
    List<Courier> availableCouriers;
    Double approxWeight = 0D;

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
        if (shipment.getBoxSize().getId().equals(EnumBoxSize.MIGRATE.getId()) || shipment.getCourier().getId().equals(EnumCourier.MIGRATE.getId())) {
            getContext().getValidationErrors().add("2", new SimpleError("None of the values can be migrate"));
        }
        Pincode pinCode = pincodeDao.getByPincode(shippingOrder.getBaseOrder().getAddress().getPin());
        if (pinCode == null) {
            getContext().getValidationErrors().add("3", new SimpleError("Pincode is invalid, It cannot be packed"));
        } else {
            boolean isCod = shippingOrder.isCOD();
            availableCouriers = courierService.getAvailableCouriers(pinCode.getPincode(), isCod);
            if (availableCouriers == null || availableCouriers.isEmpty()) {
                getContext().getValidationErrors().add("4", new SimpleError("No Couriers are applicable on this pincode, Please contact logistics, Order cannot be packed"));
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
            if (EnumShippingOrderStatus.SO_Packed.getId().equals(shippingOrder.getOrderStatus().getId())
                    || EnumShippingOrderStatus.SO_CheckedOut.getId().equals(shippingOrder.getOrderStatus().getId())
                    || EnumShippingOrderStatus.SO_Shipped.getId().equals(shippingOrder.getOrderStatus().getId())) {
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
                availableCouriers = courierService.getAvailableCouriers(pinCode.getPincode(), isCod);
                if (shippingOrder.getShipment() != null && shippingOrder.getShipment().getCourier() != null && shippingOrder.getShipment().getAwb().getAwbNumber() != null) {
                    suggestedCourier = shippingOrder.getShipment().getCourier();
                    trackingId = shippingOrder.getShipment().getAwb().getAwbNumber();
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
        Awb suggestedAwb = shipment.getAwb();
        Awb finalAwb = suggestedAwb;
        if (suggestedAwb == null || (!(suggestedAwb.getAwbNumber().equalsIgnoreCase(trackingId.trim())))) {
            if (suggestedAwb != null) {
                suggestedAwb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
                awbService.save(suggestedAwb);
            }

            List<Awb> awbList = awbService.getAvailableAwbListForCourierByWarehouseCodStatus(shipment.getCourier(), trackingId, shippingOrder.getWarehouse(), shippingOrder.isCOD(), EnumAwbStatus.Unused.getAsAwbStatus());
            if (awbList != null && awbList.size() > 0) {
                finalAwb = awbList.get(0);
            } else {
                Awb awb = new Awb();
                awb.setAwbNumber(trackingId.trim());
                awb.setAwbBarCode(trackingId.trim());
                awb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
                awb.setCourier(shipment.getCourier());
                awb.setCod(shippingOrder.isCOD());
                awb.setWarehouse(shippingOrder.getWarehouse());
                awb = awbService.save(awb);
                finalAwb = awb;
            }
        }

        finalAwb.setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
        shipment.setAwb(finalAwb);
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
}