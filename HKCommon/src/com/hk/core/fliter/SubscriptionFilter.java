package com.hk.core.fliter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.subscription.Subscription;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/9/12
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionFilter {
  private Set<Subscription> subscriptions;
  private Set<EnumSubscriptionStatus> subscriptionStatuses = new HashSet<EnumSubscriptionStatus>();

  public SubscriptionFilter(Set<Subscription> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public SubscriptionFilter addSubscriptionStatus(EnumSubscriptionStatus subscriptionStatus) {
    this.subscriptionStatuses.add(subscriptionStatus);
    return this;
  }

  public Set<Subscription> filter(){

    Set<Subscription> filteredSubscriptions = new HashSet<Subscription>();
    Set<Subscription> currentSubscriptions = new HashSet<Subscription>(subscriptions);

    if (subscriptionStatuses != null && subscriptionStatuses.size() > 0) {
      for (Subscription subscription : subscriptions) {
        List<Long> selectedSubscriptionStatusIDs = EnumSubscriptionStatus.getSubscriptionStatusIDs(subscriptionStatuses);
        if (!selectedSubscriptionStatusIDs.contains(subscription.getSubscriptionStatus().getId())) {
          currentSubscriptions.remove(subscription);
        }
      }
    }

    filteredSubscriptions.addAll(currentSubscriptions);

    return filteredSubscriptions;
  }
}
