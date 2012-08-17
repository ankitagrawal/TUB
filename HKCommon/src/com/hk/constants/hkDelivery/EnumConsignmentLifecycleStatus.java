package com.hk.constants.hkDelivery;

public enum EnumConsignmentLifecycleStatus {
    Open(10L, "Open"),
    Close(20L, "Closed"),
    OnHold(30L, "On Hold");


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
