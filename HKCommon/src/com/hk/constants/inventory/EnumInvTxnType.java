package com.hk.constants.inventory;

import com.hk.domain.core.InvTxnType;


/**
 * Generated
 */
public enum EnumInvTxnType {
    INV_CHECKIN(10L, "Fresh Inventory Checkin"),                               // +1
    INV_CHECKOUT(20L, "Inventory Checkout"),                                   // -1
    RV_CHECKIN(30L, "Checkin against Reconciliation Voucher"),                 // +1
    RV_LOST_PILFERAGE(40L, "Lost/Pilferage against Reconciliation Voucher"),   // -1
    RV_DAMAGED(45L, "Damaged Stock against Reconciliation Voucher"),           // -1
    RV_EXPIRED(50L, "Expired against Reconciliation Voucher"),                 // -1
    RTO_CHECKIN(60L, "Checkin against RTO"),                                   // +1
    CANCEL_CHECKIN(80L, "Checkin against Cancellation"),                       // +1
    TRANSIT_LOST(90L, "Lost during transit"),                                  // -1
    INV_REPEAT_CHECKOUT(100L, "Inventory Checkout against Re-shipping"),       // -1
    STOCK_TRANSFER_CHECKOUT(110L, "Inventory Checkout against Stock-Transfer"),// -1
    STOCK_TRANSFER_CHECKIN(120L, "Inventory Checkin against Stock-Transfer"),  // +1
    RV_BATCH_MISMATCH(130L, "Inventory Checkout for Batch Mismatch"),           //-1
    RV_MRP_MISMATCH(140L, "Inventory Checkout for Mrp Mismatch"),               //-1
    RV_NON_MOVING(150L, "Inventory Checkout for Non Moving Inventory"),         //-1
    RV_FREE_VARIANT_RECONCILE(160L, "Inventory Checkout for Free Variant Reconcile"),
    RV_CUSTOMER_RETURN (170L, "Inventory Checkout for Customer Return"),
    RV_PHARMA_RETURN (180L, "Inventory Checkout for Pharama Return");

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
}