package com.hk.impl.dao.subscription;

import org.springframework.stereotype.Repository;

import com.hk.domain.subscription.SubscriptionLifecycle;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.subscription.SubscriptionLifecycleDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:51:32 PM
 */
@Repository
@SuppressWarnings("unchecked")
public class SubscriptionLifecycleDaoImpl extends BaseDaoImpl implements SubscriptionLifecycleDao{

    public SubscriptionLifecycle save(SubscriptionLifecycle subscriptionLifecycle){
        return (SubscriptionLifecycle) super.save(subscriptionLifecycle);
    }

}
