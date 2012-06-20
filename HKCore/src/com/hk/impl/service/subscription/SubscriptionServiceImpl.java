package com.hk.impl.service.subscription;

import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pact.dao.subscription.SubscriptionDao;
import com.hk.domain.subscription.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:28:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionServiceImpl implements SubscriptionService{

   @Autowired
   SubscriptionDao subscriptionDao;

  @Transactional
  public Subscription save(Subscription subscription){
       return subscriptionDao.save(subscription);
  }
}
