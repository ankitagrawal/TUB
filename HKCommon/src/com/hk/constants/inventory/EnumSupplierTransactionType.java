package com.hk.constants.inventory;

import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.accounting.SupplierTransactionType;

/**
 * Generated
 */
public enum EnumSupplierTransactionType {
  Create(1L, "CREATE"),
  Purchase(2L, "PURC"),
  Payment(3L, "BP"),
  PurchaseReturn(4L, "PRT");

  private String name;
  private Long id;

  EnumSupplierTransactionType(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public SupplierTransactionType asSupplierTransactionType() {
      SupplierTransactionType supplierTransactionType = new SupplierTransactionType();
      supplierTransactionType.setId(this.getId());
      supplierTransactionType.setName(this.getName());
    return supplierTransactionType;
  }

}