package com.hk.constants.inventory;

import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.accounting.DebitNoteType;

import java.util.Arrays;
import java.util.List;

/**
 * Generated
 */
public enum EnumDebitNoteStatus {
  Created(10L, "Created"),
  HonoredBySupplier(20L, "Honored By Supplier"),
  CheckedInAsDamageInventory(30L, "CheckedIn As Damage Inventory"),
  Reconciled(40L, "Reconciled"),
  PaymentSettled(50L, "PaymentSettled"),
  ShippedToSupplier(60L,"Shipped to Supplier"),
  CLosed(100L, "Closed"),
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

  public static List<DebitNoteStatus> getAllDebitNoteStatus() {
    return Arrays.asList(Created.asDebitNoteStatus(), CheckedInAsDamageInventory.asDebitNoteStatus(), Reconciled.asDebitNoteStatus(), PaymentSettled.asDebitNoteStatus(),
        ShippedToSupplier.asDebitNoteStatus(), CLosed.asDebitNoteStatus(), Deleted.asDebitNoteStatus());
  }

}