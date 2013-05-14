package com.hk.admin.dto.inventory;

import com.hk.domain.inventory.creditNote.CreditNoteLineItem;

public class CreditNoteLineItemDto {

  private CreditNoteLineItem debitNoteLineItem;

  private Double taxable = 0.0;

  private Double tax = 0.0;

  private Double surcharge = 0.0;

  private Double payable = 0.0;

  private Long debitQty = 0L;

  public CreditNoteLineItem getCreditNoteLineItem() {
    return debitNoteLineItem;
  }

  public void setCreditNoteLineItem(CreditNoteLineItem debitNoteLineItem) {
    this.debitNoteLineItem = debitNoteLineItem;
  }

  public Double getTaxable() {
    return taxable;
  }

  public void setTaxable(Double taxable) {
    this.taxable = taxable;
  }

  public Double getTax() {
    return tax;
  }

  public void setTax(Double tax) {
    this.tax = tax;
  }

  public Double getSurcharge() {
    return surcharge;
  }

  public void setSurcharge(Double surcharge) {
    this.surcharge = surcharge;
  }

  public Double getPayable() {
    return payable;
  }

  public void setPayable(Double payable) {
    this.payable = payable;
  }

  public Long getCreditQty() {
    return debitQty;
  }

  public void setCreditQty(Long debitQty) {
    this.debitQty = debitQty;
  }
}