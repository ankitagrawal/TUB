package com.hk.constants.hkDelivery;



public enum EnumConsignmentStatus {

    ShipmentReceivedAtHub    (10L, "Received at Hub"),
    ShipmentOutForDelivery   (20L, "Out for Delivery"),
    ShipmentDelivered        (30L, "Delivered"),
    ShipmentOnHold           (40L, "On Hold by agent"),
    ShipmentLost             (50L, "Lost"),
    ShipmentDamaged          (60L, "Damaged"),
    ShipmentRTH              (70L, "Returned to hub"),
    ShipmentOnHoldByCustomer (80L, "On hold by Customer");


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
