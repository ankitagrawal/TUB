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
    DummyAwb(750L,"Dummy Awb",EnumReasonType.AwbChange),
    B2bOrder(760L,"B2b Order",EnumReasonType.AwbChange),
    ChangedByCourier(770L,"Changed By Courier",EnumReasonType.AwbChange),
    DuplicateAwb(780L,"Duplicate Awb",EnumReasonType.AwbChange),
    TechIssue(790L,"Tech Issue",EnumReasonType.AwbChange),
    ProductDamaged(800L, "Product Damaged", EnumReasonType.Reverse_Pickup_Customer),
    ProductExpired(810L, "Product Expired", EnumReasonType.Reverse_Pickup_Customer),
    WrongColor(820L, "Wrong Color", EnumReasonType.Reverse_Pickup_Customer),
    WrongSize(830L, "Wrong Size", EnumReasonType.Reverse_Pickup_Customer),
    Good(900L, "Good", EnumReasonType.Reverse_Pickup_Customer),
    Damaged(910L, "Damaged", EnumReasonType.Reverse_Pickup_Customer),
    Non_Functional(920L, "Non Functional", EnumReasonType.Reverse_Pickup_Customer),
    Near_Expiry(930L, "Near Expiry", EnumReasonType.Reverse_Pickup_Customer),

    PROD_CHANGE_CR(310L, EnumClassification.CUSTOMER_REQUEST_PRODUCT_CHANGE, EnumReasonType.Escalate_Back),
    CANCEL_CR(320L, EnumClassification.CUSTOMER_REQUEST_CANCEL, EnumReasonType.Escalate_Back),
    ON_HOLD_CR(330L, EnumClassification.CUSTOMER_REQUEST_ON_HOLD, EnumReasonType.Escalate_Back),
    PROD_DAMAGE(340L, EnumClassification.PRODUCT_NOT_AVAILABLE_DAMAGE, EnumReasonType.Escalate_Back),
    PROD_EXPIRE(350L, EnumClassification.PRODUCT_NOT_AVAILABLE_EXPIRE, EnumReasonType.Escalate_Back),
    PROD_INV_MISMATCH(360L, EnumClassification.PRODUCT_NOT_AVAILABLE_INV_MISMATCH, EnumReasonType.Escalate_Back),
    MRP_LESS(370L, EnumClassification.MRP_MISMATCH_CHEAPER, EnumReasonType.Escalate_Back),
    MRP_MORE(380L, EnumClassification.MRP_MISMATCH_DEARER, EnumReasonType.Escalate_Back),
    DISPATCH_COURIER_CHANGE(390L, EnumClassification.DISPATCH_ISSUE_COURIER_CHANGE, EnumReasonType.Escalate_Back)
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
