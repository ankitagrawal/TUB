package com.hk.pact.dao.subscription;

import java.util.List;

import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.domain.subscription.SubscriptionOrderStatus;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:48:01 PM
 */
public interface SubscriptionOrderDao extends BaseDao{

    public SubscriptionOrder save(SubscriptionOrder subscriptionOrder);

    public SubscriptionOrder findSubscriptionOrderByBaseOrder(Order order);

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription);

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription, SubscriptionOrderStatus subscriptionOrderStatus);

}
