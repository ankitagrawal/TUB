package com.hk.constants.inventory;

import com.hk.domain.accounting.DebitNoteStatus;

/**
 * Generated
 */
public enum EnumDebitNoteStatus {
  Created(10L, "Created"),
  HonoredBySupplier(20L, "Honored By Supplier"),
  CheckedInAsDamageInventory(30L, "CheckedIn As Damage Inventory"),
  Reconciled(40L, "Reconciled"),
  PaymentSettled(50L, "PaymentSettled"),
  Deleted(1000L, "Deleted"),;

  private String name;
  private Long id;

  EnumDebitNoteStatus(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public DebitNoteStatus asDebitNoteStatus() {
    DebitNoteStatus debiteNoteStatus = new DebitNoteStatus();
    debiteNoteStatus.setId(this.getId());
    debiteNoteStatus.setName(this.getName());
    return debiteNoteStatus;
  }

}