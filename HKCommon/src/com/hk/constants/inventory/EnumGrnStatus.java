package com.hk.constants.inventory;

import com.hk.domain.inventory.GrnStatus;


public enum EnumGrnStatus {
  GoodsReceived(10L, "Goods Received"),
  InventoryCheckinInProcess(20L, "Checkin In Process"),
  InventoryCheckedIn(30L, "Checkin Completed"),   //obselete
  Closed(100L, "Closed"),
  PICreated(200L, "Purchase Invoice Created"),
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