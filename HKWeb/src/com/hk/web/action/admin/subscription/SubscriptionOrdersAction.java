package com.hk.web.action.admin.subscription;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/19/12
 * Time: 7:40 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_SUBSCRIPTIONS }, authActionBean = AdminPermissionAction.class)
@Component
public class SubscriptionOrdersAction extends BaseAction{
    private Subscription subscription;
    private List<SubscriptionOrder> subscriptionOrders;

    @Autowired
    SubscriptionOrderService subscriptionOrderService;

    @DefaultHandler
    public Resolution pre(){
        subscriptionOrders=subscriptionOrderService.findSubscriptionOrdersForSubscription(subscription);
        return new ForwardResolution("/pages/admin/subscription/subscriptionOrders.jsp");
    }

    public List<SubscriptionOrder> getSubscriptionOrders() {
        return subscriptionOrders;
    }

    public void setSubscriptionOrders(List<SubscriptionOrder> subscriptionOrders) {
        this.subscriptionOrders = subscriptionOrders;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
