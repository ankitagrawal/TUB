package com.hk.pact.dao.subscription;

import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:47:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubscriptionDao extends BaseDao {

  public Subscription save(Subscription subscriptionProduct);

  public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus);
  
}
