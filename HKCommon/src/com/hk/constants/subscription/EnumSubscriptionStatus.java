package com.hk.constants.subscription;

import com.hk.domain.subscription.SubscriptionStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:54:04 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSubscriptionStatus {

    InCart(10L, "In Cart"),
    Abandoned(11L, "Abandoned"),
    Placed(15L, "Placed"),
    InProcess(20L, "In Process"),
    CustomerConfirmationAwaited(21L,"Customer Confirmation Awaited"),
    ConfirmedByCustomer(22L,"Confirmed By Customer"),
    Idle(25L, "Idle"),
    Expired(30L, "Expired"),
    Cancelled(99L, "Cancelled");

    private java.lang.String name;

    private java.lang.Long id;

    EnumSubscriptionStatus(Long id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public SubscriptionStatus asSubscriptionStatus() {
        SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
        subscriptionStatus.setId(this.getId());
        subscriptionStatus.setName(this.getName());
        return subscriptionStatus;
    }

  public static List<Long> getSubscriptionStatusIDs(Set<EnumSubscriptionStatus> subscriptionStatuses) {
    List<Long> subscriptionStatusIDs = new ArrayList<Long>();
    for (EnumSubscriptionStatus subscriptionStatus : subscriptionStatuses) {
      subscriptionStatusIDs.add(subscriptionStatus.getId());
    }
    return subscriptionStatusIDs;
  }

}
