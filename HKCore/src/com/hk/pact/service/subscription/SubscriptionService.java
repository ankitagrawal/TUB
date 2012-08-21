package com.hk.pact.service.subscription;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.User;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:28:17 PM
 */
public interface SubscriptionService {

    public Subscription save(Subscription subscription);

    public Subscription getSubscriptionFromCartLineItem(CartLineItem cartLineItem);

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus);

    public List<Subscription> getSubscriptions(Order order, EnumSubscriptionStatus subscriptionStatus);

    public List<Subscription> getSubscriptions(EnumSubscriptionStatus subscriptionStatus);

    public List<Subscription> placeSubscriptions(Order order);

    public Subscription abandonSubscription(Subscription subscription);

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage);

    public List<Subscription> searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria);

    public Page getSubscriptionsForUsers(User user, int page, int perPage);

    public int escalateSubscriptionsToActionQueue();

    public void checkInventoryForSubscriptionOrders();

    public Subscription updateSubscriptionAfterOrderDelivery(Subscription subscription);

}
