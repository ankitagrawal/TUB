package com.hk.impl.dao.subscription;

import com.akube.framework.dao.Page;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.subscription.SubscriptionStatus;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.subscription.SubscriptionProductDao;
import com.hk.pact.dao.subscription.SubscriptionDao;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:51:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
@SuppressWarnings("unchecked")
public class SubscriptionDaoImpl extends BaseDaoImpl implements SubscriptionDao {

    public Subscription save(Subscription subscription){
        return (Subscription) super.save(subscription);
    }

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus){
        return (List<Subscription>) findByNamedParams(" from Subscription sub where sub.order = :order and sub.subscriptionStatus =:subscriptionStatus ", new String[]{"order","subscriptionStatus"}, new Object[]{order, subscriptionStatus});
    }

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage){
        DetachedCriteria searchCriteria = subscriptionSearchCriteria.getSearchCriteria();
        return list(searchCriteria, true, pageNo, perPage);
    }
}
