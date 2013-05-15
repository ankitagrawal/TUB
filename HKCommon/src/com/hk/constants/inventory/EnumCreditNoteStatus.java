package com.hk.constants.inventory;

import com.hk.domain.inventory.creditNote.CreditNoteStatus;

/**
 * Generated
 */
public enum EnumCreditNoteStatus {
  Created(10L, "Created"),
  Reconciled(40L, "Reconciled"),
  Deleted(1000L, "Deleted"),;

  private String name;
  private Long id;

  EnumCreditNoteStatus(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public CreditNoteStatus asCreditNoteStatus() {
    CreditNoteStatus creditNoteStatus = new CreditNoteStatus();
    creditNoteStatus.setId(this.getId());
    creditNoteStatus.setName(this.getName());
    return creditNoteStatus

        ;
  }

}