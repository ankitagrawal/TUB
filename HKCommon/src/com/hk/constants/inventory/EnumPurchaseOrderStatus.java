package com.hk.constants.inventory;


/**
 * Generated
 */
public enum EnumPurchaseOrderStatus {
  Generated(10L, "Generated"),
  SentForApproval(20L, "Sent For Approval"),
  Approved(30L, "Approved"),
  SentToSupplier(40L, "Sent To Supplier"),
  Cancelled(100L, "Cancelled"),
  Deleted(1000L, "Deleted"),
  ;

  private String name;
  private Long id;

  EnumPurchaseOrderStatus(Long id, String name) {
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