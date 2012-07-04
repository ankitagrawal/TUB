package com.hk.web.action.core.subscription;

import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.subscription.Subscription;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/4/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionUpdateAction {
  Subscription subscription;

  @Autowired
  SubscriptionService subscriptionService;


  @JsonHandler
  public Resolution pre() {
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "");


    return new JsonResolution(healthkartResponse);
  }

  @JsonHandler
  public Resolution abandonSubscription(){
    subscriptionService.abandonSubscription(subscription);
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "");


    return new JsonResolution(healthkartResponse);
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setSubscription(Subscription subscription) {
    this.subscription = subscription;
  }

  public SubscriptionService getSubscriptionService() {
    return subscriptionService;
  }

  public void setSubscriptionService(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }
}
