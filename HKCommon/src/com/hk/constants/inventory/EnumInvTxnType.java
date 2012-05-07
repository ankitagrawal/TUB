package com.hk.constants.inventory;


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
  ;

  private String name;
  private Long id;

  EnumInvTxnType(Long id, String name) {
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