package com.hk.web.action.admin.order;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.CancellationType;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 2 Feb, 2010 12:10:16 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_ORDER }, authActionBean = AdminPermissionAction.class)
@Component
public class CancelOrderAction extends BaseAction {
    @Autowired
    AdminOrderService             adminOrderService;
    @Autowired
    private UserService      userService;
    @Autowired
    private PaymentService paymentService;

    @Validate(required = true)
    private Order            order;

    @Validate(required = true)
    private CancellationType cancellationType;

    private String           cancellationRemark;

    //@Validate(required = true, on = "pre")
    private Long reconciliationType;

    @JsonHandler
    public Resolution pre() {
        User loggedOnUser = userService.getLoggedInUser();
        // TODO: need to tighten logic for cancellation of order
        adminOrderService.cancelOrder(order, cancellationType, cancellationRemark, loggedOnUser);
        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(order.getOrderStatus()));
        if (EnumOrderStatus.Cancelled.getId().equals(order.getOrderStatus().getId())) {
            if (paymentService.isValidReconciliation(order.getPayment()) && reconciliationType != null) {
                boolean flag = paymentService.reconciliationOnCancel(reconciliationType, order, order.getPayment().getAmount(), cancellationRemark);
                if (EnumReconciliationType.RewardPoints.getId().equals(reconciliationType) && flag) {
                    adminOrderService.logOrderActivity(order, EnumOrderLifecycleActivity.RewardPointOrderCancel);
                    addRedirectAlertMessage(new SimpleMessage("Reward Point awarded to customer"));
                } else if (EnumReconciliationType.RefundAmount.getId().equals(reconciliationType) && flag) {
                    adminOrderService.logOrderActivity(order,EnumOrderLifecycleActivity.AmountRefundedOrderCancel);
                    addRedirectAlertMessage(new SimpleMessage("Amount Refunded to customer"));
                } else if (EnumReconciliationType.RefundAmount.getId().equals(reconciliationType) && !flag){
                    adminOrderService.logOrderActivity(order,EnumOrderLifecycleActivity.RefundAmountFailed);
                    addRedirectAlertMessage(new SimpleMessage("Amount couldn't be refunded to user"));
                } else {
                    adminOrderService.logOrderActivity(order, EnumOrderLifecycleActivity.RefundAmountExceedsFailed);
                    addRedirectAlertMessage(new SimpleMessage("Amount exceeds the refundable amount"));
                }
            }
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "cancelled", data);
        return new JsonResolution(healthkartResponse);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCancellationType(CancellationType cancellationType) {
        this.cancellationType = cancellationType;
    }

    public void setCancellationRemark(String cancellationRemark) {
        this.cancellationRemark = cancellationRemark;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Long getReconciliationType() {
        return reconciliationType;
    }

    public void setReconciliationType(Long reconciliationType) {
        this.reconciliationType = reconciliationType;
    }
}
