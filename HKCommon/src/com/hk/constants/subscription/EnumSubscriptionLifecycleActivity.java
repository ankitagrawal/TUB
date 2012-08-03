package com.hk.constants.subscription;

import com.hk.domain.subscription.SubscriptionLifecycle;
import com.hk.domain.subscription.SubscriptionLifecycleActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/28/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSubscriptionLifecycleActivity {
    AddedToCart(5L,"Subscription added to Cart"),
    SubscriptionAbandoned(6L,"Subscription Abandoned"),
    SubscriptionPlaced(10L, "Subscription Placed"),
    AddressChanged(15L, "Address Changed"),
    ConfirmedSubscriptionOrder(16L, "Confirmed Subscription Order"),
    EscalatedToActionQueue(17L,"Subscription Escalated to action queue"),
    NextShipmentDateChanged(20L, "Next Shipment Date Changed"),
    SubscriptionOrderPlaced(25L, "Subscription Order Placed"),
    SubscriptionOrderDelivered(30L, "Subscription Order Delivered"),
    SubscriptionExpired(40L, "Subscription Expired"),
    OutOfStockEmailSent(50L, "Product Variant Out of Stock Email"),
    LoggedComment(120L, "Logged Comment"),
    SubscriptionCancelled(199L, "Subscription Cancelled");

    private String name;
    private Long id;

    EnumSubscriptionLifecycleActivity(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public SubscriptionLifecycleActivity asSubscriptionLifecycleActivity(){
        SubscriptionLifecycleActivity subscriptionLifecycleActivity=new SubscriptionLifecycleActivity();
        subscriptionLifecycleActivity.setId(this.id);
        subscriptionLifecycleActivity.setName(this.name);
        return subscriptionLifecycleActivity;
    }
}
