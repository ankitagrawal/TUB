package com.hk.pact.dao.subscription;

import com.hk.domain.subscription.SubscriptionLifecycle;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:47:46 PM
 */
public interface SubscriptionLifecycleDao extends BaseDao {

    public SubscriptionLifecycle save(SubscriptionLifecycle subscriptionLifecycle);

}
