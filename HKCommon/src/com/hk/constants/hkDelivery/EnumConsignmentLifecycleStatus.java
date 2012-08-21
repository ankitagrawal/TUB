package com.hk.constants.hkDelivery;

public enum EnumConsignmentLifecycleStatus {

    ReceivedAtHub(10L, "Received at Hub"),
    Dispatched(20L, "Dispatched"),
    ReturnedToHub(30L, "Returned to Hub"),
    ReturnedToSource(40L, "Returned to Source"),
    ConsignmentLost(50L, "Consignment Lost"),
    Hold(60L, "Hold"),
    Damaged(70L, "Consignment Lost"),
    Delivered(80L, "Delivered to Customer");


    private Long id;
    private String status;


    EnumConsignmentLifecycleStatus(Long id, String status) {
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
