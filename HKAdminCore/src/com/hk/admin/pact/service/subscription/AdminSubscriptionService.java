package com.hk.admin.pact.service.subscription;

import com.hk.domain.subscription.Subscription;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 8/2/12
 * Time: 1:43 PM
 */
public interface AdminSubscriptionService {

    public Double getRewardPointsForSubscriptionCancellation(Subscription subscription);

    public boolean sendSubscriptionCancellationEmails(Subscription subscription);

    public Subscription cancelSubscription(Subscription subscription,String cancellationRemark);

}
