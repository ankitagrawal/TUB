package com.hk.admin.dto.inventory;

import com.hk.domain.accounting.DebitNoteLineItem;

public class DebitNoteLineItemDto {

  private DebitNoteLineItem debitNoteLineItem;

  private Double taxable = 0.0;

  private Double tax = 0.0;

  private Double surcharge = 0.0;

  private Double payable = 0.0;

  private Long debitQty = 0L;
  
  private Double discountPercent = 0.0;

  public DebitNoteLineItem getDebitNoteLineItem() {
    return debitNoteLineItem;
  }

  public void setDebitNoteLineItem(DebitNoteLineItem debitNoteLineItem) {
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

  public Long getDebitQty() {
    return debitQty;
  }

  public void setDebitQty(Long debitQty) {
    this.debitQty = debitQty;
  }

public Double getDiscountPercent() {
	return discountPercent;
}

public void setDiscountPercent(Double discountPercent) {
	this.discountPercent = discountPercent;
}
}