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
    RV_SUBTRACT_DAMAGED(45L, "Damaged Stock against RV Subtract"),           // -1
    RV_SUBTRACT_EXPIRED(50L, "Expired against RV Subtract"),                 // -1
    RTO_CHECKIN(60L, "Checkin against RTO"),                                   // +1
    CANCEL_CHECKIN(80L, "Checkin against Cancellation"),                       // +1
    TRANSIT_LOST(90L, "Lost during transit"),                                  // -1
    INV_REPEAT_CHECKOUT(100L, "Inventory Checkout against Re-shipping"),       // -1
    STOCK_TRANSFER_CHECKOUT(110L, "Inventory Checkout against Stock-Transfer"),// -1
    STOCK_TRANSFER_CHECKIN(120L, "Inventory Checkin against Stock-Transfer"),  // +1
    RV_SUBTRACT_BATCH_MISMATCH(130L, "rv subtract Inventory for Batch Mismatch"),           //-1
    RV_MRP_MISMATCH(140L, "Inventory Checkout for Mrp Mismatch"),               //-1
    RV_NON_MOVING(150L, "Inventory Checkout for Non Moving Inventory"),         //-1
    RV_SUBTRACT_FREE_VARIANT_RECONCILE(160L, "rv subtract for Free Variant Recon"),
    RV_CUSTOMER_RETURN(170L, "Inventory Checkout for Customer Return"),
    RV_PHARMA_RETURN(180L, "Inventory Checkout for Pharama Return"),
    PRODUCT_VARIANT_AUDITED(190L, "Product Variant Audited"),
    RV_ADD_FREE_VARIANT_RECONCILE(200L, "rv subtract  for Free Variant Recon"),
    RV_ADD_DAMAGED(210L, "Damaged Stock against RV Add"),           // +1
    RV_ADD_EXPIRED(220L, "Expired against RV Add"),
    RV_ADD_BATCH_MISMATCH(230L, "rv add Inventory for Batch Mismatch"),
    RV_ADD_EXPIRED_AUTOMATIC_DELETION(240L, "rv add automatic deletion for Expired"),
    INCORRECT_COUNTING(250L, "rv for Incorrect Counting");               //+1 & -1

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