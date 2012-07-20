package com.hk.pact.dao.subscription;

import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:48:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubscriptionOrderDao extends BaseDao{

    public SubscriptionOrder save(SubscriptionOrder subscriptionOrder);

    public SubscriptionOrder findSubscriptionOrderByBaseOrder(Order order);

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription);

}
