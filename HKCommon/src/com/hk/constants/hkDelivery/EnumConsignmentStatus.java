package com.hk.constants.hkDelivery;

import com.hk.domain.hkDelivery.ConsignmentStatus;

import java.util.Arrays;
import java.util.List;


public enum EnumConsignmentStatus {

    ShipmentReceivedAtHub       (10L, "Shipment Received at Hub"),
    ShipmentOutForDelivery   (20L, "Shipment out for Delivery"),
    ShipmentDelivered        (30L, "Shipment Delivered"),
    ShipmentOnHold          (40L, "Shipment on Hold"),
    ShipmentLost            (50L, "Shipment Lost"),
    ShipmentDamaged         (60L, "Shipment Damaged"),
    ShipmentRTO            (70L, "Shipment Returned");


    private java.lang.Long id;
    private java.lang.String status;


    EnumConsignmentStatus(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
