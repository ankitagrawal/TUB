package com.hk.impl.dao.subscription;

import com.hk.domain.order.Order;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.subscription.SubscriptionOrderDao;
import org.springframework.stereotype.Repository;

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
        return (SubscriptionOrder) super.save(subscriptionOrder);
    }

    public SubscriptionOrder findSubscriptionOrderByBaseOrder(Order order){
        return (SubscriptionOrder) findUniqueByNamedParams("from SubscriptionOrder so where so.baseOrder = :order ", new String[]{"order"}, new Object[]{order});
    }
}
