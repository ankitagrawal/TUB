package com.hk.impl.service.subscription;

import com.hk.constants.subscription.EnumSubscriptionOrderStatus;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pact.dao.subscription.SubscriptionDao;
import com.hk.domain.subscription.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:28:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService{

  @Autowired
  SubscriptionDao subscriptionDao;

  @Transactional
  public Subscription save(Subscription subscription){
    return subscriptionDao.save(subscription);
  }

  public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus){
    return subscriptionDao.getSubscriptions(order,subscriptionStatus);
  }

  public List<Subscription> getSubscriptions(Order order, EnumSubscriptionStatus subscriptionStatus){
    return subscriptionDao.getSubscriptions(order,subscriptionStatus.asSubscriptionStatus());
  }

  public List<Subscription> placeSubscriptions(Order order){
    List<Subscription> inCartSubscriptions= getSubscriptions(order, EnumSubscriptionStatus.InCart.asSubscriptionStatus());
    for(Subscription subscription : inCartSubscriptions){
      subscription.setSubscriptionStatus(EnumSubscriptionStatus.Placed.asSubscriptionStatus());
      subscription.setAddress(order.getAddress());
      subscriptionDao.save(subscription);
    }
    return  inCartSubscriptions;
  }

  public Subscription abandonSubscription(Subscription subscription){
    subscription.setSubscriptionStatus(EnumSubscriptionStatus.Abandoned.asSubscriptionStatus());
    this.save(subscription);
    return subscription;
  }
}
