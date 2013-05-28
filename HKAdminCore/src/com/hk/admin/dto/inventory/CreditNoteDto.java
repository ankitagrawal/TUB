package com.hk.admin.dto.inventory;

import com.hk.domain.inventory.creditNote.CreditNote;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 2, 2011
 * Time: 10:38:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreditNoteDto {

  private CreditNote creditNote;

  private List<CreditNoteLineItemDto> creditNoteLineItemDtoList = new ArrayList<CreditNoteLineItemDto>();

  private Double totalTax = 0.0;

  private Double totalPayable = 0.0;

  private Double totalTaxable = 0.0;

  private Double totalSurcharge = 0.0;

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

  public CreditNote getCreditNote() {
    return creditNote;
  }

  public void setCreditNote(CreditNote creditNote) {
    this.creditNote = creditNote;
  }

  public List<CreditNoteLineItemDto> getCreditNoteLineItemDtoList() {
    return creditNoteLineItemDtoList;
  }

  public void setCreditNoteLineItemDtoList(List<CreditNoteLineItemDto> creditNoteLineItemDtoList) {
    this.creditNoteLineItemDtoList = creditNoteLineItemDtoList;
  }

  public Double getTotalSurcharge() {
    return totalSurcharge;
  }

  public void setTotalSurcharge(Double totalSurcharge) {
    this.totalSurcharge = totalSurcharge;
  }
}