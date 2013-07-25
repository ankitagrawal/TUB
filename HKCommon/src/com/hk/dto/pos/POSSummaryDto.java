package com.hk.dto.pos;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/21/13
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class POSSummaryDto {
  private Double cashAmountCollected;
  private Double cashAmountRefunded;
  private Double creditCardAmountCollected;
  private Double creditCardAmountRefunded;
  private Double totalCollection;
  private Double avgAmtPerInvoice;
  private Long itemsSold;
  private Long itemsReturned;
  private Long noOfBills;

  public POSSummaryDto(Double cashAmountCollected, Double cashAmountRefunded, Double creditCardAmountCollected,
                       Double creditCardAmountRefunded, Long itemsSold, Long itemsReturned,
                       Double totalCollection, Double avgAmtPerInvoice, Long noOfBills) {
    this.cashAmountCollected = cashAmountCollected;
    this.cashAmountRefunded = cashAmountRefunded;
    this.creditCardAmountCollected = creditCardAmountCollected;
    this.creditCardAmountRefunded = creditCardAmountRefunded;
    this.itemsSold = itemsSold;
    this.itemsReturned = itemsReturned;
    this.totalCollection = totalCollection;
    this.avgAmtPerInvoice = avgAmtPerInvoice;
    this.itemsReturned = itemsReturned;
    this.noOfBills=noOfBills;
  }

  public Double getAvgAmtPerInvoice() {
    return avgAmtPerInvoice;
  }

  public void setAvgAmtPerInvoice(Double avgAmtPerInvoice) {
    this.avgAmtPerInvoice = avgAmtPerInvoice;
  }

  public Double getCashAmountCollected() {
    return cashAmountCollected;
  }

  public void setCashAmountCollected(Double cashAmountCollected) {
    this.cashAmountCollected = cashAmountCollected;
  }

  public Double getCashAmountRefunded() {
    return cashAmountRefunded;
  }

  public void setCashAmountRefunded(Double cashAmountRefunded) {
    this.cashAmountRefunded = cashAmountRefunded;
  }

  public Double getCreditCardAmountCollected() {
    return creditCardAmountCollected;
  }

  public void setCreditCardAmountCollected(Double creditCardAmountCollected) {
    this.creditCardAmountCollected = creditCardAmountCollected;
  }

  public Double getCreditCardAmountRefunded() {
    return creditCardAmountRefunded;
  }

  public void setCreditCardAmountRefunded(Double creditCardAmountRefunded) {
    this.creditCardAmountRefunded = creditCardAmountRefunded;
  }

  public Long getItemsSold() {
    return itemsSold;
  }

  public void setItemsSold(Long itemsSold) {
    this.itemsSold = itemsSold;
  }

  public Long getItemsReturned() {
    return itemsReturned;
  }

  public void setItemsReturned(Long itemsReturned) {
    this.itemsReturned = itemsReturned;
  }

  public Double getTotalCollection() {
    return totalCollection;
  }

  public void setTotalCollection(Double totalCollection) {
    this.totalCollection = totalCollection;
  }

  public Long getNoOfBills() {
    return noOfBills;
  }

  public void setNoOfBills(Long noOfBills) {
    this.noOfBills = noOfBills;
  }
}
