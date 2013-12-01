package com.hk.web.action.admin.order;

import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderLifecycle;
import com.hk.domain.user.User;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_ORDERS }, authActionBean = AdminPermissionAction.class)
@Component
public class OrderLifecycleAction extends BaseAction {

    private Order               order;

    @Autowired
    private OrderLoggingService orderLoggingService;

    @Validate(required = true)
    private String              comment;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/orderLifecycle.jsp");
    }

    public Resolution saveComment() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }

        OrderLifecycle orderLifecycle = new OrderLifecycle();
        orderLifecycle.setOrder(order);
        orderLifecycle.setOrderLifecycleActivity(getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.LoggedComment));
        orderLifecycle.setUser(loggedOnUser);
        orderLifecycle.setComments(comment);
        orderLifecycle.setActivityDate(new Date());
        getBaseDao().save(orderLifecycle);

        addRedirectAlertMessage(new SimpleMessage("Comment saved successfully."));
        return new RedirectResolution(OrderLifecycleAction.class).addParameter("order", order);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

}