package com.hk.constants.analytics;

import com.hk.constants.queue.EnumClassification;
import com.hk.domain.analytics.Reason;

import java.util.Arrays;

import java.util.List;

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
    HighShippingCost(180L, "High Shipping Cost", EnumReasonType.NotAutoEscalated),
    DiscrepancyInPaymentAmount(190L, "Discrepancy In Payable vs Paid Amount", EnumReasonType.NotAutoEscalated),
    ShipmentNotCreatedManual(210L, "Shipment Not Created", EnumReasonType.NotManualEscalated),
    InvalidPaymentStatusManual(220L, "Payment Status is AuthPending/Error", EnumReasonType.NotManualEscalated),
    InsufficientUnbookedInventoryManual(230L, "Insufficient Unbooked Inventory", EnumReasonType.NotManualEscalated),

    CourierServiceChange(700L, "Courier Service Change", EnumReasonType.CourierChange),
    ChangeFromGroundToAirShipping(710L, "Change From Ground To Air Shipping", EnumReasonType.CourierChange),
    CcRequest(720L, "CC Request", EnumReasonType.CourierChange),
    BrightMovement(730L, "Bright Movement", EnumReasonType.CourierChange),
    RtoDueToOda(740L, "RTO Due To ODA", EnumReasonType.CourierChange),
    DummyAwb(750L, "Dummy Awb", EnumReasonType.AwbChange),
    B2bOrder(760L, "B2b Order", EnumReasonType.AwbChange),
    ChangedByCourier(770L, "Changed By Courier", EnumReasonType.AwbChange),
    DuplicateAwb(780L, "Duplicate Awb", EnumReasonType.AwbChange),
    TechIssue(790L, "Tech Issue", EnumReasonType.AwbChange),
    ProductDamaged(800L, "Product Damaged", EnumReasonType.Reverse_Pickup_Customer),
    ProductExpired(810L, "Product Expired", EnumReasonType.Reverse_Pickup_Customer),
    WrongColor(820L, "Wrong Color", EnumReasonType.Reverse_Pickup_Customer),
    WrongSize(830L, "Wrong Size", EnumReasonType.Reverse_Pickup_Customer),
    Good(900L, "Good", EnumReasonType.Reverse_Pickup_Warehouse),
    Damaged(910L, "Damaged", EnumReasonType.Reverse_Pickup_Warehouse),
    Non_Functional(920L, "Non Functional", EnumReasonType.Reverse_Pickup_Warehouse),
    Near_Expiry(930L, "Near Expiry", EnumReasonType.Reverse_Pickup_Warehouse),
    Expired(940L, "Expired", EnumReasonType.Reverse_Pickup_Warehouse),
    RefundFailed(1100L,"Refund Failed",EnumReasonType.Reconciliation),
    RefundSuccessful(1110L,"Refund Successful", EnumReasonType.Reconciliation),
    RewardGiven(1120L,"Reward Points Given", EnumReasonType.Reconciliation),
    RewardNotGiven(1130L,"Reward Points Not Given", EnumReasonType.Reconciliation),
    RefundInProcess(1140L,"Refund in process",EnumReasonType.Reconciliation),
    ManualRefundInitiated(1150L, "Manual refund task mail sent to admin", EnumReasonType.Reconciliation),
    NoActionTakenAtReconciliation(1450L, "No Action Taken", EnumReasonType.Reconciliation),
    ;


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
//        reason.setClassification(enumClassification.asClassification());
        reason.setType(reasonType);
        return reason;
    }

    public static EnumReason getById(Long id) {
        EnumReason enumReasonById = null;
        for (EnumReason enumReason : EnumReason.values()) {
            if (enumReason.getId().equals(id)) {
                enumReasonById = enumReason;
            }
        }
        return enumReasonById;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

}
