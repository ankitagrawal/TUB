package com.hk.web.action.admin.payment;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_PAYMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class CheckPaymentAction extends BaseAction {

    @Validate(required = true)
    private Order                order;

    private List<Payment>        paymentList;

    @Validate(required = true, on = { "acceptAsAuthPending", "acceptAsSuccessful" })
    private Payment              payment;

    private PricingDto           pricingDto;

    @Autowired
    private PaymentService       paymentService;
    @Autowired
    private OrderLoggingService  orderLoggingService;
    @Autowired
    private OrderService         orderService;
    @Autowired
    private OrderStatusService   orderStatusService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private OrderManager         orderManager;
    @Autowired
    private PricingEngine        pricingEngine;
    @Autowired
    private PaymentManager       paymentManager;

    // PaymentManager paymentManager;

    // InventoryService inventoryService;

    // EmailManager emailManager;

    // OrderService orderService;

    @DefaultHandler
    public Resolution show() {
        paymentList = getPaymentService().listByOrderId(order.getId());

        payment = order.getPayment();

        if (EnumOrderStatus.InCart.getId().equals(order.getOrderStatus().getId())) {
            // User user = order.getUser();
            // OfferInstance offerInstance = order.getOfferInstance();
            pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), 0D), order.getAddress());
        } else {
            pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());
        }

        return new ForwardResolution("/pages/admin/checkPayment.jsp");
    }

    public Resolution acceptAsAuthPending() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        getPaymentManager().pendingApproval(payment.getGatewayOrderId());
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentMarkedAuthPending), null);
        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.auth"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
    }

    public Resolution acceptAsSuccessful() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        getPaymentManager().success(payment.getGatewayOrderId());
        orderService.sendEmailToServiceProvidersForOrder(order);
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentMarkedSuccessful), null);
        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.received"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
    }

    public Resolution associateToPayment() {
        getPaymentManager().associateToOrder(payment.getGatewayOrderId());
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentAssociatedToOrder), null);

        /*
         * //Auto escalation of order if unbooked inventory is positive if (order.getShippingOrders() != null &&
         * !order.getShippingOrders().isEmpty()) { for (ShippingOrder shippingOrder : order.getShippingOrders()) {
         * shippingOrderService.autoEscalateShippingOrder(shippingOrder); }
         * //orderService.escalateOrderFromActionQueue(order); } else if (order.getShippingOrders() == null &&
         * order.getShippingOrders().isEmpty()) { orderService.splitOrder(order); }
         */

        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.associated"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
    }

    public Resolution updateToSuccess() {
        Payment payment = order.getPayment();
        payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.SUCCESS));
        getPaymentService().save(payment);

        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentUpdatedAsSuccessful), null);

        orderService.sendEmailToServiceProvidersForOrder(order);
        orderService.processOrderForAutoEsclationAfterPaymentConfirmed(order);

        /*
         * //Auto escalation of order if unbooked inventory is positive if (order.getShippingOrders() != null &&
         * !order.getShippingOrders().isEmpty()) { for (ShippingOrder shippingOrder : order.getShippingOrders()) {
         * shippingOrderService.autoEscalateShippingOrder(shippingOrder); } } else if (order.getShippingOrders() == null &&
         * order.getShippingOrders().isEmpty()) { orderService.splitOrder(order); }
         */

        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.received"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
        // return new RedirectResolution(getPreviousBreadcrumb().getUrl(), false);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PricingDto getPricingDto() {
        return pricingDto;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public PricingEngine getPricingEngine() {
        return pricingEngine;
    }

    public void setPricingEngine(PricingEngine pricingEngine) {
        this.pricingEngine = pricingEngine;
    }

    public void setPricingDto(PricingDto pricingDto) {
        this.pricingDto = pricingDto;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public void setPaymentManager(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

}
