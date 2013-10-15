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
    AwbChange(60L, "Awb Change Reason"),
    So_Cancelled(410L,"SO  Cancelled"),
    Reverse_Pickup_Customer(420L,"Customer Reason For Reverse Pickup"),
    Reconciliation(430L,"Reconciliation"),
    Reverse_Pickup_Warehouse(440L,"Warehouse QA For Reverse pickup"),
    SO_NOT_CANCELLED(450L,"SO Could not be cancelled automatically."),
    REFUND(460L,"Refund initiated manually"),
    ShipmentNotCreatedFromFitnessPro(470L,"Shipment Not Created from Fitness Pro")
    ;

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
