package com.hk.constants.analytics;

/*
 * User: Pratham
 * Date: 26/03/13  Time: 01:06
*/
public enum  EnumReasonType {

    Escalate_Back(10L, "SO Escalated Back To Action Queue"),
    ShipmentNotCreated(20L, "SO Shipment Not Created"),
    NotAutoEscalated(30L, "SO Could not be Auto-escalated"),
    NotManualEscalated(40L, "SO Could not be Manually-escalated"),
    CourierChange(50L, "Courier Change Reason"),
    AwbChange(60L, "Awb Change Reason");
    private String name;

    private Long id;

    EnumReasonType(Long id, String name) {
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
