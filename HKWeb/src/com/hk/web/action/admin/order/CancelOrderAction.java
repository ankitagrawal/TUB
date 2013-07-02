package com.hk.web.action.admin.order;

import java.util.HashMap;
import java.util.Map;

import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

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
    @Autowired
    private RewardPointService rewardPointService;

    @Validate(required = true)
    private Order            order;

    @Validate(required = true)
    private CancellationType cancellationType;

    private String           cancellationRemark;

    @Validate(required = true)
    private String reconciliationType;

    @JsonHandler
    public Resolution pre() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        if(reconciliationType.equalsIgnoreCase("0")) {
            //TODO:
        } else {
            if(paymentService.isRefundAmountValid(order.getGatewayOrderId(),order.getAmount())){
                try {
                    paymentService.refundPayment(order.getGatewayOrderId(), order.getAmount());
                    // TODO: get the Refund Payment object if unsuccessful then again ask for Reward Points
                } catch (HealthkartPaymentGatewayException e){
                    //TODO:
                }

            } else {
                addRedirectAlertMessage(new SimpleMessage("Total Refund Amount cannot exceed base order amount"));
            }
        }

        // TODO: need to tighten logic for cancellation of order
        adminOrderService.cancelOrder(order, cancellationType, cancellationRemark, loggedOnUser);
        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(order.getOrderStatus()));
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

    public String getReconciliationType() {
        return reconciliationType;
    }

    public void setReconciliationType(String reconciliationType) {
        this.reconciliationType = reconciliationType;
    }
}
