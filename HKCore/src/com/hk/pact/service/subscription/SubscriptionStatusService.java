package com.hk.pact.service.subscription;

import java.util.List;

import com.hk.domain.subscription.SubscriptionStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/24/12
 * Time: 5:30 PM
 */
public interface SubscriptionStatusService {

    public List<SubscriptionStatus> getSubscriptionStatusesForUsers();
}
