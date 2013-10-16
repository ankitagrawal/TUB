package com.hk.constants.inventory;

import com.hk.domain.core.InvTxnType;

import java.util.List;
import java.util.Arrays;


/**
 * Generated
 */
public enum EnumInvTxnType {
    INV_CHECKIN(10L, "Fresh Inventory Checkin"),                               // +1
    INV_CHECKOUT(20L, "Inventory Checkout"),                                   // -1
    RV_CHECKIN(30L, "Checkin against Reconciliation Voucher"),                 // +1
    RV_LOST_PILFERAGE(40L, "Lost/Pilferage against Reconciliation Voucher"),   // -1
    RV_SUBTRACT_DAMAGED(45L, "Damaged Stock against Reconciliation Voucher-Subtract"),           // -1
    RV_SUBTRACT_EXPIRED(50L, "Expired against Reconciliation Voucher-Subtract"),                 // -1
    RETURN_CHECKIN_DAMAGED(60L, "Damaged against Returned items"),     // 0
    RETURN_CHECKIN_EXPIRED(65L, "Expired against Returned items"),     // 0
    RETURN_CHECKIN_GOOD(70L, "Checkin against Returned items"),                // +1
    CANCEL_CHECKIN(80L, "Checkin against Cancellation"),                       // +1
    TRANSIT_LOST(90L, "Lost during transit"),                                  // -1
    INV_REPEAT_CHECKOUT(100L, "Inventory Checkout against Re-shipping"),       // -1
    STOCK_TRANSFER_CHECKOUT(110L, "Inventory Checkout against Stock-Transfer"),// -1
    STOCK_TRANSFER_CHECKIN(120L, "Inventory Checkin against Stock-Transfer"),  // +1
    RV_SUBTRACT_BATCH_MISMATCH(130L, "Inventory Checkout for Batch Mismatch-RV Subtract"),           //-1
    RV_MRP_MISMATCH(140L, "Inventory Checkout for Mrp Mismatch"),               //-1
    RV_NON_MOVING(150L, "Inventory Checkout for Non Moving Inventory"),         //-1
    RV_SUBTRACT_FREE_VARIANT_RECONCILE(160L, "Inventory Checkout for Free Variant-RV Subtract"), //-1
    RV_CUSTOMER_RETURN(170L, "Inventory Checkin for Customer Return"), // +1
    RV_PHARMA_RETURN(180L, "Inventory Checkin for Pharama Return"),    // +1
    PRODUCT_VARIANT_AUDITED(190L, "Product Variant Audited-RV Subtract"),          // -1
    RV_ADD_INCORRECT_COUNTING(200L, "RV Add Incorrect Counting "),
    RV_SUBTRACT_INCORRECT_COUNTING(210L, "RV Subtract Incorrect Counting"),

    RV_ADD_DAMAGED(220L, "Damaged Stock against Reconciliation Voucher-Add"),           // +1
    RV_ADD_EXPIRED(230L, "Expired against Reconciliation Voucher-Add"),
    RV_ADD_FREE_VARIANT_RECONCILE(240L, "Inventory Checkin for Free Variant-RV Add"),
    RV_ADD_BATCH_MISMATCH(250L, "Inventory Checkin for Batch Mismatch-RV Add"),
    RV_ADD_EXPIRED_AUTOMATIC_DELETION(260L, "RV Add automatic deletion for Expired"),
    REVERSE_PICKUP_INVENTORY_CHECKIN(270L, "Reverse Pickup Inventory CheckIn"),
    RV_SUBTRACT_DAMAGE_LOGISTICS(280L, "RV Subtract Damage Logistics"),
    RV_ADD_VENDOR_REPLACEMENT(290L,"RV Add Vendor Replacement"),
    RV_ADD_VENDOR_REJECTED(300L,"RV Add Vendor Rejected"),
    RV_ADD_FOUND_ADD(310L,"RV Add Found Add")
  ;


    private String name;
    private Long id;

    EnumInvTxnType(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public InvTxnType asInvTxnType() {
        InvTxnType invTxnType = new InvTxnType();
        invTxnType.setId(this.getId());
        invTxnType.setName(this.getName());
        return invTxnType;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public static List<Long> getIdsForReturnedGoodsTxnType() {
        return Arrays.asList(RETURN_CHECKIN_GOOD.getId(), RETURN_CHECKIN_DAMAGED.getId(), RETURN_CHECKIN_EXPIRED.getId());
    }
}