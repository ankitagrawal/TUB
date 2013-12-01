package com.hk.constants.subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.hk.domain.subscription.SubscriptionStatus;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:54:04 PM
 */
public enum EnumSubscriptionStatus {

    InCart(10L, "In Cart"),
    Abandoned(15L, "Abandoned"),
    Placed(20L, "Placed"),
    InProcess(25L, "In Process"),
    CustomerConfirmationAwaited(30L,"Customer Confirmation Awaited"),
    ConfirmedByCustomer(35L,"Confirmed By Customer"),
    Idle(40L, "Idle"),
    Expired(45L, "Expired"),
    OnHold(50L,"On Hold"),
    OutOfStock(55L,"Out Of Stock"),
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

    public static List<EnumSubscriptionStatus> getStatusForCustomers() {
        return Arrays.asList(EnumSubscriptionStatus.Placed,
                EnumSubscriptionStatus.Cancelled,
                EnumSubscriptionStatus.InProcess,
                EnumSubscriptionStatus.OnHold,
                EnumSubscriptionStatus.CustomerConfirmationAwaited,
                EnumSubscriptionStatus.ConfirmedByCustomer,
                EnumSubscriptionStatus.Idle,
                EnumSubscriptionStatus.OutOfStock,
                EnumSubscriptionStatus.Expired);
    }

}
