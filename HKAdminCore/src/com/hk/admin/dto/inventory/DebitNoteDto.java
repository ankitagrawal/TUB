package com.hk.admin.dto.inventory;

import java.util.ArrayList;
import java.util.List;

import com.hk.domain.accounting.DebitNote;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 2, 2011
 * Time: 10:38:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DebitNoteDto {

  private DebitNote debitNote;

  private List<DebitNoteLineItemDto> debitNoteLineItemDtoList = new ArrayList<DebitNoteLineItemDto>();

  private Double totalTax = 0.0;

  private Double totalPayable = 0.0;

  private Double totalTaxable = 0.0;

  private Double totalSurcharge = 0.0;
  
  private Double totalDiscount = 0.0D;
  
  private Double finalDebitAmount = 0.0D;
  
  private Double freightForwardingCharges = 0.0D;

  public Double getTotalTax() {
    return totalTax;
  }

  public void setTotalTax(Double totalTax) {
    this.totalTax = totalTax;
  }

  public Double getTotalPayable() {
    return totalPayable;
  }

  public void setTotalPayable(Double totalPayable) {
    this.totalPayable = totalPayable;
  }

  public Double getTotalTaxable() {
    return totalTaxable;
  }

  public void setTotalTaxable(Double totalTaxable) {
    this.totalTaxable = totalTaxable;
  }

  public DebitNote getDebitNote() {
    return debitNote;
  }

  public void setDebitNote(DebitNote debitNote) {
    this.debitNote = debitNote;
  }

  public List<DebitNoteLineItemDto> getDebitNoteLineItemDtoList() {
    return debitNoteLineItemDtoList;
  }

  public void setDebitNoteLineItemDtoList(List<DebitNoteLineItemDto> debitNoteLineItemDtoList) {
    this.debitNoteLineItemDtoList = debitNoteLineItemDtoList;
  }

  public Double getTotalSurcharge() {
    return totalSurcharge;
  }

  public void setTotalSurcharge(Double totalSurcharge) {
    this.totalSurcharge = totalSurcharge;
  }

public Double getTotalDiscount() {
	return totalDiscount;
}

public void setTotalDiscount(Double totalDiscount) {
	this.totalDiscount = totalDiscount;
}

public Double getFinalDebitAmount() {
	return finalDebitAmount;
}

public void setFinalDebitAmount(Double finalDebitAmount) {
	this.finalDebitAmount = finalDebitAmount;
}

public Double getFreightForwardingCharges() {
	return freightForwardingCharges;
}

public void setFreightForwardingCharges(Double freightForwardingCharges) {
	this.freightForwardingCharges = freightForwardingCharges;
}
}