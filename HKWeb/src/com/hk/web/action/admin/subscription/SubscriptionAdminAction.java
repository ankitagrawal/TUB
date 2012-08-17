package com.hk.web.action.admin.subscription;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.service.subscription.AdminSubscriptionService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/15/12
 * Time: 3:07 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_SUBSCRIPTION }, authActionBean = AdminPermissionAction.class)
@Component
public class SubscriptionAdminAction extends BaseAction implements ValidationErrorHandler{
    @Autowired
    SubscriptionOrderService subscriptionOrderService;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    AdminSubscriptionService adminSubscriptionService;
    @Autowired
    SubscriptionLoggingService subscriptionLoggingService;

    @Validate(required = true)
    private Subscription subscription;

    @Validate(required = true, on="changeNextShipmentDate")
    private Date nextShipmentDate;

    private String cancellationRemark;

    @JsonHandler
    public Resolution confirmedByCustomer(){
        User loggedOnUser = getUserService().getLoggedInUser();
        Map<String, Object> data = new HashMap<String, Object>(2);
        if (EnumSubscriptionStatus.CustomerConfirmationAwaited.getId().equals(subscription.getSubscriptionStatus().getId())) {
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.ConfirmedByCustomer.asSubscriptionStatus());
            subscriptionService.save(subscription);
            data.put("subscriptionStatus", JsonUtils.hydrateHibernateObject(subscription.getSubscriptionStatus()));
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);

            subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.ConfirmedSubscriptionOrder);

            subscriptionOrderService.createOrderForSubscription(subscription);


            return new JsonResolution(healthkartResponse);
        } else {
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_RELOAD, "check your subscription", data);
            return new JsonResolution(healthkartResponse);
        }
    }

    @JsonHandler
    public Resolution putOnHold(){
        User loggedOnUser = getUserService().getLoggedInUser();
        Map<String, Object> data = new HashMap<String, Object>(2);
        if (EnumSubscriptionStatus.CustomerConfirmationAwaited.getId().equals(subscription.getSubscriptionStatus().getId())) {
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.OnHold.asSubscriptionStatus());
            subscriptionService.save(subscription);

            subscriptionOrderService.createOrderForSubscription(subscription);

            data.put("subscriptionStatus", JsonUtils.hydrateHibernateObject(subscription.getSubscriptionStatus()));
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
            return new JsonResolution(healthkartResponse);
        } else{
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_RELOAD, "check your subscription", data);
            return new JsonResolution(healthkartResponse);
        }
    }

    public Resolution changeAddress(){
        return new RedirectResolution(ChangeSubscriptionAddressAction.class).addParameter("subscription",subscription);
    }

    @DontValidate
    public Resolution escalateSubscriptions(){
        subscriptionService.checkInventoryForSubscriptionOrders();
        subscriptionService.escalateSubscriptionsToActionQueue();
        return new ForwardResolution("/pages/admin/subscription/searchSubscription.jsp");
    }

    @JsonHandler
    public Resolution changeNextShipmentDate(){
        try{
            subscription.setNextShipmentDate(nextShipmentDate);
            if(subscription.getSubscriptionStatus().getId()==EnumSubscriptionStatus.CustomerConfirmationAwaited.getId()){
                subscription.setSubscriptionStatus(EnumSubscriptionStatus.Idle.asSubscriptionStatus());
            }
            subscriptionService.save(subscription);
            subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.NextShipmentDateChanged);

            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success");
            return new JsonResolution(healthkartResponse);
        }catch (Exception e){
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_RELOAD, "some problem.. please check");
            return new JsonResolution(healthkartResponse);
        }
    }

    @JsonHandler
    public Resolution cancelSubscription(){
        try{
            subscription = adminSubscriptionService.cancelSubscription(subscription,cancellationRemark);
            Map<String, Object> data = new HashMap<String, Object>(1);
            data.put("subscriptionStatus", JsonUtils.hydrateHibernateObject(subscription.getSubscriptionStatus()));
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "cancelled",data);
            return new JsonResolution(healthkartResponse);
        }catch (Exception e){
            getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
            return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
        }
    }

    @JsonHandler
    public Resolution rewardPointsOnSubscriptionCancellation(){
        try{
            if(subscription.getSubscriptionStatus().getId().longValue()==EnumSubscriptionStatus.Cancelled.getId().longValue()){
                Double rewardPoints = adminSubscriptionService.getRewardPointsForSubscriptionCancellation(subscription);
                Map<String, Object> data = new HashMap<String, Object>(1);
                data.put("rewardPoints", JsonUtils.hydrateHibernateObject(rewardPoints));
                HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "cancelled",data);
                return new JsonResolution(healthkartResponse);
            }else{
                HealthkartResponse healthkartResponse =new HealthkartResponse(HealthkartResponse.STATUS_ERROR,"error");
                return new JsonResolution(healthkartResponse);
            }

        }catch (Exception e){
            getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
            return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
        }
    }


    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Date getNextShipmentDate() {
        return nextShipmentDate;
    }

    public void setNextShipmentDate(Date nextShipmentDate) {
        this.nextShipmentDate = nextShipmentDate;
    }

    public String getCancellationRemark() {
        return cancellationRemark;
    }

    public void setCancellationRemark(String cancellationRemark) {
        this.cancellationRemark = cancellationRemark;
    }
}
