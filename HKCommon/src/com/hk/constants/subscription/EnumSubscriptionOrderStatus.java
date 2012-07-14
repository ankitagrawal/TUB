package com.hk.constants.subscription;

import com.hk.domain.subscription.SubscriptionOrderStatus;
import com.hk.domain.subscription.SubscriptionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:53:26 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSubscriptionOrderStatus {

    Placed(10L, "Placed"),
    InProcess(15L, "In Process"),
    Delivered(20L, "Shipped"),
    Cancelled(99L, "Cancelled");

    private java.lang.String name;

    private java.lang.Long id;

    EnumSubscriptionOrderStatus(Long id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public SubscriptionOrderStatus asSubscriptionOrderStatus() {
        SubscriptionOrderStatus subscriptionOrderStatus = new SubscriptionOrderStatus();
        subscriptionOrderStatus.setId(this.getId());
        subscriptionOrderStatus.setName(this.getName());
        return subscriptionOrderStatus;
    }

}
