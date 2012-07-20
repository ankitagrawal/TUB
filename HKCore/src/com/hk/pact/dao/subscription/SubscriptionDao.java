package com.hk.pact.dao.subscription;

import com.akube.framework.dao.Page;
import com.hk.constants.subscription.EnumSubscriptionOrderStatus;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:47:24 PM
 */
public interface SubscriptionDao extends BaseDao {

    public Subscription save(Subscription subscriptionProduct);

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus);

    public List<Subscription> getSubscriptions(EnumSubscriptionStatus subscriptionStatus);

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage);

    public int escalateSubscriptionsToActionQueue(List<SubscriptionStatus> fromStatuses, SubscriptionStatus toStatus, Date referenceDate);

}
