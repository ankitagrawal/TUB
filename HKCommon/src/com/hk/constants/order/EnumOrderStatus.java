package com.hk.constants.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.core.OrderStatus;


public enum EnumOrderStatus {

    InCart(10L, "In Cart"),   // when base order is created for a user for the first time
    Placed(15L, "Placed"),    //when user has confirmed the order on website by making payment
    InProcess(20L, "In Process"),      // when base order has been split into one or more shipping orders, this means base order is in process
    OnHold(25L, "On Hold"),
    Shipped(30L, "Shipped"),
    Delivered(40L, "Delivered"),
    Installed (42L,"Installed"),
    PartialRTO(45L, "Partially Returned"),
    RTO(50L, "Returned"),
    Lost(60L, "Lost"),
    PartiallyLost(70L, "Partially Lost"),
    SubscriptionInProgress(80L,"Subscription in Process"),
    Cancelled(99L, "Cancelled");

    private java.lang.String name;

    private java.lang.Long id;

    EnumOrderStatus(Long id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public OrderStatus asOrderStatus() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(this.getId());
        orderStatus.setName(this.getName());
        return orderStatus;
    }

    public static List<Long> getOrderStatusIDs(List<EnumOrderStatus> enumOrderStatuses) {
        List<Long> orderStatusIds = new ArrayList<Long>();
        for (EnumOrderStatus enumOrderStatus : enumOrderStatuses) {
            orderStatusIds.add(enumOrderStatus.getId());
        }
        return orderStatusIds;
    }


    public static List<EnumOrderStatus> getStatusForActionQueue() {
        return Arrays.asList(EnumOrderStatus.InProcess, EnumOrderStatus.OnHold, EnumOrderStatus.Placed);
    }

    public static List<EnumOrderStatus> getStatusForCustomers() {
        return Arrays.asList(EnumOrderStatus.Placed,
                EnumOrderStatus.Cancelled,
                EnumOrderStatus.InProcess,
                EnumOrderStatus.OnHold,
                EnumOrderStatus.Shipped,
                EnumOrderStatus.Delivered,
                EnumOrderStatus.RTO,
                EnumOrderStatus.SubscriptionInProgress);
    }


    public static List<EnumOrderStatus> getStatusForReporting() {
        return Arrays.asList(
                EnumOrderStatus.InProcess,
                EnumOrderStatus.Placed,
                EnumOrderStatus.OnHold,
                EnumOrderStatus.Shipped,
                EnumOrderStatus.Delivered
        );
    }
}


