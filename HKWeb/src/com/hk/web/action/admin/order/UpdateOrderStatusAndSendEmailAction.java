package com.hk.web.action.admin.order;

import java.util.List;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.service.shippingOrder.ShipmentServiceImpl;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.SMSManager;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.store.StoreService;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_SHIPMENT_QUEUE }, authActionBean = AdminPermissionAction.class)
@Component
public class UpdateOrderStatusAndSendEmailAction extends BaseAction {
    @Autowired
    private EmailManager         emailManager;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private LinkManager          linkManager;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private SubscriptionOrderService subscriptionOrderService;
	@Autowired
    private SMSManager smsManager;

    public Resolution pre() {
        List<ShippingOrder> shippingOrderList = this.shippingOrderService.getShippingOrdersToSendShipmentEmail();

        for (ShippingOrder shippingOrder : shippingOrderList) {
            Shipment shipment = shippingOrder.getShipment();
            Order order = shippingOrder.getBaseOrder();
            boolean isEmailSent = false;
            if (order.getStore() != null && order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID) && !order.isSubscriptionOrder()) {
                isEmailSent = this.emailManager.sendOrderShippedEmail(shippingOrder, this.linkManager.getShippingOrderInvoiceLink(shippingOrder));
	            this.smsManager.sendOrderShippedSMS(shippingOrder);

            }else if(order.getStore() != null && order.getStore().getId().equals(StoreService.LOYALTYPG_ID)) {
            	isEmailSent = this.emailManager.sendOrderShippedEmail(shippingOrder, this.linkManager.getShippingOrderInvoiceLink(shippingOrder));
  	            this.smsManager.sendOrderShippedSMS(shippingOrder);
            }else if(order.isSubscriptionOrder()){
                isEmailSent = this.emailManager.sendSubscriptionOrderShippedEmail(shippingOrder,this.getSubscriptionOrderService().findSubscriptionOrderByBaseOrder(shippingOrder.getBaseOrder()).getSubscription(), this.linkManager.getShippingOrderInvoiceLink(shippingOrder));
            }else {
                isEmailSent = true; // Incase on non HK order
            }
            if (isEmailSent && shipment != null) {
                shipment.setEmailSent(true);
                this.shipmentService.save(shipment);
            }
            this.shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShippedEmailFired);
        }

        this.addRedirectAlertMessage(new SimpleMessage("Shipping emails for [" + shippingOrderList.size() + "] Shipping Orders have been sent."));
        return new RedirectResolution(AdminHomeAction.class);
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    public void setShipmentService(ShipmentServiceImpl shipmentService) {
        this.shipmentService = shipmentService;
    }

    public SubscriptionOrderService getSubscriptionOrderService() {
        return this.subscriptionOrderService;
    }

    public void setSubscriptionOrderService(SubscriptionOrderService subscriptionOrderService) {
        this.subscriptionOrderService = subscriptionOrderService;
    }
}
