package com.hk.web.action.admin.subscription;

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
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionLifecycle;
import com.hk.domain.user.User;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/16/12
 * Time: 1:18 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_SUBSCRIPTIONS }, authActionBean = AdminPermissionAction.class)
@Component
public class SubscriptionLifecycleAction extends BaseAction {


    @Autowired
    private SubscriptionLoggingService subscriptionLoggingService;

    private Subscription subscription;

    @Validate(required = true, on="saveComment")
    private String              comment;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/subscription/subscriptionLifeCycle.jsp");
    }

    public Resolution saveComment() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }

        SubscriptionLifecycle subscriptionLifecycle= new SubscriptionLifecycle();
        subscriptionLifecycle.setSubscription(subscription);
        subscriptionLifecycle.setSubscriptionLifecycleActivity(EnumSubscriptionLifecycleActivity.LoggedComment.asSubscriptionLifecycleActivity());
        subscriptionLifecycle.setUser(loggedOnUser);
        subscriptionLifecycle.setComments(comment);
        subscriptionLifecycle.setDate(new Date());
        subscriptionLoggingService.save(subscriptionLifecycle);

        addRedirectAlertMessage(new SimpleMessage("Comment saved successfully."));
        return new RedirectResolution(SubscriptionLifecycleAction.class).addParameter("subscription", subscription);
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
