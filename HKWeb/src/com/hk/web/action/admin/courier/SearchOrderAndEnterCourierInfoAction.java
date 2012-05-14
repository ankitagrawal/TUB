package com.hk.web.action.admin.courier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.BoxSize;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.report.manager.ReportManager;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class SearchOrderAndEnterCourierInfoAction extends BaseAction {

    private static Logger              logger            = LoggerFactory.getLogger(SearchOrderAndEnterCourierInfoAction.class);

    List<ShippingOrder>                shippingOrderList = new ArrayList<ShippingOrder>(0);
    ShippingOrder                      shippingOrder;
    @Autowired
    ShippingOrderDao                   shippingOrderDao;
    @Autowired
    ShippingOrderService               shippingOrderService;
    @Autowired
    UserService                        userService;
    @Autowired
    PincodeDao                         pincodeDao;

    private String                     gatewayOrderId;
    Courier                            suggestedCourier;
    List<Courier>                      availableCouriers;
    BoxSize                            boxSize;
    Double                             boxWeight;
    String                             trackingId;
    Shipment                           shipment;

    // @Named(Keys.Env.adminDownloads)
    @Value("#{hkEnvProps['adminDownloads']}")
    String                             adminDownloads;
    File                               xlsFile;

    @Autowired
    private ReportManager                      reportGenerator;
    @Autowired
    private CourierService             courierService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_PACKING_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchOrderAndEnterCouierInfo.jsp");
    }

    public Resolution searchOrders() {

        shippingOrder = shippingOrderDao.findByGatewayOrderId(gatewayOrderId);
        /*
         * if (shippingOrder == null) { addRedirectAlertMessage(new SimpleMessage("Shipping Order not found for the
         * corresponding gateway order id")); return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class); }
         * else if (shippingOrder.getOrderStatus() != null) { Long id = shippingOrder.getOrderStatus().getId(); if (id <
         * EnumShippingOrderStatus.SO_CheckedOut.getId()) { addRedirectAlertMessage(new SimpleMessage("Shipping Order is
         * not checkout out yet .It cannot be packed. ")); return new
         * RedirectResolution(SearchOrderAndEnterCourierInfoAction.class); } else if (id >=
         * EnumShippingOrderStatus.SO_Delivered.getId()) { addRedirectAlertMessage(new SimpleMessage("Shipping Order was
         * either delieverd/canceled .Courier details cannot be changed. ")); return new
         * RedirectResolution(SearchOrderAndEnterCourierInfoAction.class); } else { shipment =
         * shippingOrder.getShipment(); shippingOrderList.add(shippingOrder); } }
         */
        if (shippingOrder == null) {
            addRedirectAlertMessage(new SimpleMessage("Shipping Order not found for the corresponding gateway order id"));
            return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
        } else {
            if (EnumShippingOrderStatus.SO_Packed.getId().equals(shippingOrder.getOrderStatus().getId())
                    || EnumShippingOrderStatus.SO_CheckedOut.getId().equals(shippingOrder.getOrderStatus().getId())
                    || EnumShippingOrderStatus.SO_Shipped.getId().equals(shippingOrder.getOrderStatus().getId())) {
                shipment = shippingOrder.getShipment();
                shippingOrderList.add(shippingOrder);
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
                suggestedCourier = courierService.getDefaultCourierByPincodeAndWarehouse(pinCode, isCod);
                // if (suggestedCourier == null) {
                // suggestedCourier = courierService.getSuggestedCourierService(pinCode.getPincode(), isCod);
                // }
            }
        } catch (Exception e) {
            logger.error("Error while getting suggested courier for shippingOrder#" + shippingOrder.getId(), e);
        }
        return new ForwardResolution("/pages/admin/searchOrderAndEnterCouierInfo.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PACKING_QUEUE }, authActionBean = AdminPermissionAction.class)
    public Resolution saveShipmentDetails() {
        // TODO: Verify if the logic is working fine
        if (StringUtils.isBlank(shipment.getTrackingId()) || shipment.getBoxWeight() == null) {
            addRedirectAlertMessage(new SimpleMessage("Tracking Id, Box weight are also mandatory"));
            return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class).addParameter("searchOrders").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
        }
        shipment.setEmailSent(false);
        shippingOrder.setShipment(shipment);
        shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Packed));
        shippingOrderDao.save(shippingOrder);
        String comment = "";
        if (shipment != null) {
            comment = "Shipment Details: " + shipment.getCourier().getName() + "/" + shipment.getTrackingId();
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

    public Courier getSuggestedCourier() {
        return suggestedCourier;
    }

    public void setSuggestedCourier(Courier suggestedCourier) {
        this.suggestedCourier = suggestedCourier;
    }

    public BoxSize getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(BoxSize boxSize) {
        this.boxSize = boxSize;
    }

    public Double getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(Double boxWeight) {
        this.boxWeight = boxWeight;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
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
}