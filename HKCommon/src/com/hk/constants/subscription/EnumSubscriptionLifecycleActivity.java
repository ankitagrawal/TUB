package com.hk.constants.subscription;

import com.hk.domain.subscription.SubscriptionLifecycleActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/28/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSubscriptionLifecycleActivity {
    AddedToCart(10L,"Subscription added to Cart"),
    SubscriptionAbandoned(20L,"Subscription Abandoned"),
    SubscriptionPlaced(30L, "Subscription Placed"),
    AddressChanged(40L, "Address Changed"),
    ConfirmedSubscriptionOrder(50L, "Confirmed Subscription Order"),
    EscalatedToActionQueue(60L,"Subscription Escalated to action queue"),
    NextShipmentDateChanged(70L, "Next Shipment Date Changed"),
    SubscriptionOrderPlaced(80L, "Subscription Order Placed"),
    SubscriptionOrderDelivered(90L, "Subscription Order Delivered"),
    SubscriptionExpired(100L, "Subscription Expired"),
    OutOfStockEmailSent(110L, "Product Variant Out of Stock Email"),
    LoggedComment(120L, "Logged Comment"),
    SubscriptionCancelled(999L, "Subscription Cancelled");

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
