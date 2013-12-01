package com.hk.web.action.admin.payment;

import java.util.HashMap;
import java.util.Map;

import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.manager.SMSManager;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.User;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 12 Feb, 2010 6:04:16 PM
 */
@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_ORDER}, authActionBean = AdminPermissionAction.class)
@Component
public class VerifyCodAction extends BaseAction {
    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    AdminOrderService adminOrderService;

    @Validate(required = true)
    private Order order;
    @Autowired
    SMSManager smsManager;

    @JsonHandler
    public Resolution pre() {
        User loggedOnUser = getUserService().getLoggedInUser();
        Map<String, Object> data = new HashMap<String, Object>(2);
        if (EnumPaymentStatus.AUTHORIZATION_PENDING.getId().equals(order.getPayment().getPaymentStatus().getId())) {
            String comment = "Confirmed by Clicking link Confirm COD";
            Payment payment = adminOrderService.confirmCodOrder(order, comment, loggedOnUser);
            if (payment != null) {
                data.put("paymentStatus", JsonUtils.hydrateHibernateObject(payment.getPaymentStatus()));
                data.put("orderStatus", JsonUtils.hydrateHibernateObject(order.getOrderStatus()));
                HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
                return new JsonResolution(healthkartResponse);
            }
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_RELOAD, "Payment Already Verified", data);
        return new JsonResolution(healthkartResponse);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public void setPaymentManager(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }


    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }


}
