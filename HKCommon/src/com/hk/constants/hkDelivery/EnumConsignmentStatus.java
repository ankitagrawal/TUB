package com.hk.constants.hkDelivery;


public enum EnumConsignmentStatus {

    ShipmntRcvdAtHub     (10L, "Shipment Received at Hub"),
    ShipmntOutForDelivry (20L, "Shipment out for Delivery"),
    ShpmntDelivered      (30L, "Shipment Delivered"),
    ShipmntOnHold        (40L, "Shipment on Hold"),
    ShipmntLost          (50L, "Shipment Lost"),
    ShipmntDamaged       (60L, "Shipment Damaged"),
    ShipmentRTO          (70L, "Shipment Returned");


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
