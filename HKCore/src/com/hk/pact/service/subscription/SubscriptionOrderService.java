package com.hk.pact.service.subscription;

import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.domain.user.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/13/12
 * Time: 6:56 PM
 */
public interface SubscriptionOrderService {

    public SubscriptionOrder save(SubscriptionOrder subscriptionOrder);

    public Order createOrderForSubscription(Subscription subscription);

    public List<Order> createOrdersForSubscriptions(List<Subscription> subscriptions);

}
