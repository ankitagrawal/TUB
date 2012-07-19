package com.hk.web.action.admin.shipment;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_DELIVERY_QUEUE }, authActionBean = AdminPermissionAction.class)
public class ChangeShipmentDetailsAction extends BaseAction {

    private ShippingOrder shippingOrder;
    private Shipment      shipment;
    private String        originalShippingOrderStatus;
    private String        gatewayOrderId;
    String                comments;
    private boolean       visible = false;
    private static Logger logger  = LoggerFactory.getLogger(ChangeShipmentDetailsAction.class);

    @Autowired
    ShippingOrderService  shippingOrderService;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/changeShipmentDetails.jsp");
    }

    public Resolution search() {
        if (gatewayOrderId != null) {
            shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
            if (shippingOrder != null) {
                if (shippingOrder.getOrderStatus().getId() >= EnumShippingOrderStatus.SO_Shipped.getId()) {
                    originalShippingOrderStatus = shippingOrder.getOrderStatus().getName();
                    shipment = shippingOrder.getShipment();
                    visible = true;
                    return new ForwardResolution("/pages/admin/changeShipmentDetails.jsp");
                } else {
                    addRedirectAlertMessage(new SimpleMessage("Order with given id is not yet shipped"));
                    return new ForwardResolution("/pages/admin/changeShipmentDetails.jsp");
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("Order with given id does not exist"));
                return new ForwardResolution("/pages/admin/changeShipmentDetails.jsp");
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please enter gateway order Id"));
            return new ForwardResolution("/pages/admin/changeShipmentDetails.jsp");
        }
    }

    public Resolution save() {

        shippingOrder.setShipment(shipment);
        shippingOrderService.save(shippingOrder);
        // comments = "Courier:" + shipment.getCourier().getName() + ", TrackingId:" + shipment.getTrackingId() +
        // ", Status:" + shippingOrder.getOrderStatus().getName();
        String shippingOrderStatus = shippingOrder.getOrderStatus().getName();
        if (!originalShippingOrderStatus.equals(shippingOrderStatus)) {
            comments = "Status changed from " + originalShippingOrderStatus + " to " + shippingOrder.getOrderStatus().getName();
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_StatusChanged, comments);
        }
        addRedirectAlertMessage(new SimpleMessage("Changes Saved."));
        return new ForwardResolution("/pages/admin/changeShipmentDetails.jsp");
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

    public boolean isVisible() {
        return visible;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public String getOriginalShippingOrderStatus() {
        return originalShippingOrderStatus;
    }

    public void setOriginalShippingOrderStatus(String originalShippingOrderStatus) {
        this.originalShippingOrderStatus = originalShippingOrderStatus;
    }
    // public String getComments() {
    // return comments;
    // }
    //
    // public void setComments(String comments) {
    // this.comments = comments;
    // }
}
