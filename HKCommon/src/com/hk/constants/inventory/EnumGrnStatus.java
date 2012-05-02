package com.hk.constants.inventory;

import com.hk.domain.inventory.GrnStatus;


public enum EnumGrnStatus {
  GoodsReceived(10L, "Goods Received"),
  InventoryCheckedIn(20L, "Inventory Checked In"),
  EscToAccounts(25L, "Esc To Accounts"),
  Reconciled(30L, "Reconciled"),
  PaymentSettled(40L, "PaymentSettled"),
  Deleted(1000L, "Deleted"),;

  private String name;
  private Long id;

  EnumGrnStatus(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public GrnStatus asGrnStatus() {
    GrnStatus grnStatus = new GrnStatus();
    grnStatus.setId(this.getId());
    grnStatus.setName(this.getName());
    return grnStatus;
  }

}