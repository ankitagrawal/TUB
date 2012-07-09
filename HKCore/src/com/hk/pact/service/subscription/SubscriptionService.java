package com.hk.pact.service.subscription;

import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;

import java.util.List;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:28:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubscriptionService {

  public Subscription save(Subscription subscription);

  public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus);

  public List<Subscription> getSubscriptions(Order order, EnumSubscriptionStatus subscriptionStatus);

  public List<Subscription> placeSubscriptions(Order order);

  public Subscription abandonSubscription(Subscription subscription);
  
}
