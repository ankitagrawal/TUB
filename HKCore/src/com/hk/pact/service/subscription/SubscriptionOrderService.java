package com.hk.pact.service.subscription;

import java.util.List;

import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.domain.subscription.SubscriptionOrderStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/13/12
 * Time: 6:56 PM
 */
public interface SubscriptionOrderService {

    public SubscriptionOrder save(SubscriptionOrder subscriptionOrder);

    public SubscriptionOrder findSubscriptionOrderByBaseOrder(Order order);

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription);

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription, SubscriptionOrderStatus subscriptionOrderStatus);

    public Order createOrderForSubscription(Subscription subscription);

    public List<Order> createOrdersForSubscriptions(List<Subscription> subscriptions);

    public void markSubscriptionOrderAsDelivered(Order order);

    public void markSubscriptionOrderAsShipped(Order order);

}
