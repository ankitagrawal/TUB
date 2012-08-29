package com.hk.impl.dao.subscription;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.subscription.SubscriptionDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:51:44 PM
 */
@Repository
@SuppressWarnings("unchecked")
public class SubscriptionDaoImpl extends BaseDaoImpl implements SubscriptionDao {

    public Subscription save(Subscription subscription){
        subscription.setUpdateDate(BaseUtils.getCurrentTimestamp());
        return (Subscription) super.save(subscription);
    }

    public Subscription getSubscriptionFromCartLineItem(CartLineItem cartLineItem){
        return (Subscription) findUniqueByNamedParams(" from Subscription sub where sub.cartLineItem = :cartLineItem", new String[]{"cartLineItem"}, new Object[]{cartLineItem});
    }

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus){
        return (List<Subscription>) findByNamedParams(" from Subscription sub where sub.baseOrder = :order and sub.subscriptionStatus =:subscriptionStatus ", new String[]{"order","subscriptionStatus"}, new Object[]{order, subscriptionStatus});
    }

    public List<Subscription> getSubscriptions(EnumSubscriptionStatus subscriptionStatus){
        return (List<Subscription>) findByNamedParams(" from Subscription sub where sub.subscriptionStatus =:subscriptionStatus ", new String[]{"subscriptionStatus"}, new Object[]{subscriptionStatus.asSubscriptionStatus()});
    }

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage){
        DetachedCriteria searchCriteria = subscriptionSearchCriteria.getSearchCriteria();
        return list(searchCriteria, true, pageNo, perPage);
    }

    public Page getSubscriptionsForUsers(List<SubscriptionStatus> subscriptionStatusList,User user, int page, int perPage){
        DetachedCriteria criteria = DetachedCriteria.forClass(Subscription.class);
        criteria.add(Restrictions.in("subscriptionStatus", subscriptionStatusList));
        criteria.add(Restrictions.eq("user", user));
        criteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
        return list(criteria, page, perPage);
    }

    public List<Subscription> searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria){
        DetachedCriteria searchCriteria=subscriptionSearchCriteria.getSearchCriteria();
        searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return findByCriteria(searchCriteria);
    }

}
