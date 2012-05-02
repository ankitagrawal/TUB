package com.hk.constants.inventory;

import com.hk.domain.inventory.rv.ReconciliationStatus;

public enum EnumReconciliationStatus {
  PENDING(10L, "N"),
  DONE(20L, "Y")
  ;


  private java.lang.String name;
  private java.lang.Long id;

  EnumReconciliationStatus(java.lang.Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }

  public ReconciliationStatus asReconciliationStatus() {
    ReconciliationStatus reconciliationStatus = new ReconciliationStatus();
    reconciliationStatus.setId(this.getId());
    reconciliationStatus.setName(this.getName());
    return reconciliationStatus;
  }

}
