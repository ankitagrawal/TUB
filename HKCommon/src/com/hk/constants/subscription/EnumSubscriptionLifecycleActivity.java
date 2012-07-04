package com.hk.constants.subscription;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/28/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSubscriptionLifecycleActivity {
    SubscriptionPlaced(10L, "Subscription Placed"),
    AddressChanged(15L, "Address Changed"),
    NextShipmentDateChanged(20L, "Next Shipment Date Changed"),
    SubscriptionCancelled(99L, "Subscription Cancelled");

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
}
