package com.hk.impl.dao.subscription;

import org.springframework.stereotype.Repository;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.subscription.SubscriptionProductDao;
import com.hk.pact.dao.subscription.SubscriptionDao;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;

import java.util.List;

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
}
