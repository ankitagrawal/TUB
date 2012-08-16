package com.hk.pact.dao.subscription;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:47:24 PM
 */
public interface SubscriptionDao extends BaseDao {

    public Subscription save(Subscription subscriptionProduct);

    public Subscription getSubscriptionFromCartLineItem(CartLineItem cartLineItem);

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus);

    public List<Subscription> getSubscriptions(EnumSubscriptionStatus subscriptionStatus);

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage);

    public Page getSubscriptionsForUsers(List<SubscriptionStatus> subscriptionStatusList, User user, int page, int perPage);

    public List<Subscription> searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria);

}
