package com.hk.pact.service.subscription;

import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionLifecycle;
import com.hk.domain.subscription.SubscriptionLifecycleActivity;
import com.hk.domain.user.User;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/16/12
 * Time: 12:38 PM
 */
public interface SubscriptionLoggingService {

    public SubscriptionLifecycle save(SubscriptionLifecycle subscriptionLifecycle);

    public void logSubscriptionActivity(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity);

    public void logSubscriptionActivityByAdmin(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity);

    public void logSubscriptionActivity(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity,String comments);

    public void logSubscriptionActivityByAdmin(Subscription subscription, EnumSubscriptionLifecycleActivity enumSubscriptionLifecycleActivity, String comments);

    public void logSubscriptionActivity(Subscription subscription, User user, SubscriptionLifecycleActivity subscriptionLifecycleActivity, String comments);
}
