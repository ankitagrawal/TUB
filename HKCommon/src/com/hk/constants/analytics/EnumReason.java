package com.hk.constants.analytics;

import com.hk.constants.queue.EnumClassification;
import com.hk.domain.analytics.Reason;

/*
 * User: Pratham
 * Date: 26/03/13  Time: 23:00
*/
public enum EnumReason {

    PINCODE_INVALID(10L, "Invalid Pincode", EnumReasonType.ShipmentNotCreated),
    SUGGESTED_COURIER_NOT_FOUND(20L, "Suggested Courier not found", EnumReasonType.ShipmentNotCreated),
    COURIER_SERVICE_INFO_NOT_FOUND(30L, "Courier service not available here", EnumReasonType.ShipmentNotCreated),
    DROP_SHIPPED_ORDER(40L, "No shipment for drop shipped orders", EnumReasonType.ShipmentNotCreated),
    AWB_NOT_ASSIGNED(50L, "Could not assign Awb", EnumReasonType.ShipmentNotCreated),
    BO_Referred_Order(110L, "BO is a referred Order", EnumReasonType.NotAutoEscalated),
    Drop_Shipped_Order(120L, "Drop Shipped Order", EnumReasonType.NotAutoEscalated),
    Contains_Jit_Products(130L, "Order contains JIT Products", EnumReasonType.NotAutoEscalated),
    Contains_Prescription_Glasses(140L, "Contains prescription glasses", EnumReasonType.NotAutoEscalated),
    InsufficientUnbookedInventory(150L, "Insufficient Unbooked Inventory", EnumReasonType.NotAutoEscalated),
    InvalidPaymentStatus(160L, "Payment Status is AuthPending/Error", EnumReasonType.NotAutoEscalated),
    ShipmentNotCreated(170L, "Shipment Not Created", EnumReasonType.NotAutoEscalated),
    ShipmentNotCreatedManual(210L, "Shipment Not Created", EnumReasonType.NotManualEscalated),
    InvalidPaymentStatusManual(220L, "Payment Status is AuthPending/Error", EnumReasonType.NotManualEscalated),
    InsufficientUnbookedInventoryManual(230L, "Insufficient Unbooked Inventory", EnumReasonType.NotManualEscalated);


    Long id;
    EnumClassification enumClassification;
    String reasonType;

    //todo ps migration
    EnumReason(Long id, String primaryClassification, EnumReasonType enumReasonType) {
        this.id = id;
        this.enumClassification = enumClassification;
        this.reasonType = enumReasonType.getName();
    }

    EnumReason(Long id, EnumClassification enumClassification, EnumReasonType enumReasonType) {
        this.id = id;
        this.enumClassification = enumClassification;
        this.reasonType = enumReasonType.getName();
    }

    public Reason asReason() {
        Reason reason = new Reason();
        reason.setId(id);
        reason.setClassification(enumClassification.asClassification());
        reason.setType(reasonType);
        return reason;
    }


}
