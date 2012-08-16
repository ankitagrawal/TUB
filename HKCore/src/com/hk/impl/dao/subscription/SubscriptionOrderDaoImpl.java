package com.hk.impl.dao.subscription;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.domain.subscription.SubscriptionOrderStatus;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.subscription.SubscriptionOrderDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/14/12
 * Time: 12:06 AM
 */
@Repository
@SuppressWarnings("unchecked")
public class SubscriptionOrderDaoImpl extends BaseDaoImpl implements SubscriptionOrderDao {

    public SubscriptionOrder save(SubscriptionOrder subscriptionOrder){
        subscriptionOrder.setUpdateDate(BaseUtils.getCurrentTimestamp());
        return (SubscriptionOrder) super.save(subscriptionOrder);
    }

    public SubscriptionOrder findSubscriptionOrderByBaseOrder(Order order){
        return (SubscriptionOrder) findUniqueByNamedParams("from SubscriptionOrder so where so.baseOrder = :order ", new String[]{"order"}, new Object[]{order});
    }

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription){
        return (List<SubscriptionOrder>) findByNamedParams("from SubscriptionOrder so where so.subscription = :subscription ", new String[]{"subscription"}, new Object[]{subscription});
    }

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription, SubscriptionOrderStatus subscriptionOrderStatus){
        return (List<SubscriptionOrder>) findByNamedParams("from SubscriptionOrder so where so.subscription = :subscription and so.subscriptionOrderStatus = :subscriptionOrderStatus", new String[]{"subscription","subscriptionOrderStatus"}, new Object[]{subscription,subscriptionOrderStatus});
    }
}
