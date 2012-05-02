package com.hk.report.dto.order;

import java.util.Date;


public class ShipmentDto {

  Date orderDate;
  Long txnCount;
  Long codTxnCount;
  Long shippedSameDay;
  Long shippedNextDay;
  Long shippedOnDayTwo;

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public Long getTxnCount() {
    return txnCount;
  }

  public void setTxnCount(Long txnCount) {
    this.txnCount = txnCount;
  }

  public Long getShippedSameDay() {
    return shippedSameDay;
  }

  public void setShippedSameDay(Long shippedSameDay) {
    this.shippedSameDay = shippedSameDay;
  }

  public Long getShippedNextDay() {
    return shippedNextDay;
  }

  public void setShippedNextDay(Long shippedNextDay) {
    this.shippedNextDay = shippedNextDay;
  }

  public Long getShippedOnDayTwo() {
    return shippedOnDayTwo;
  }

  public void setShippedOnDayTwo(Long shippedOnDayTwo) {
    this.shippedOnDayTwo = shippedOnDayTwo;
  }

  public Long getCodTxnCount() {
    return codTxnCount;
  }

  public void setCodTxnCount(Long codTxnCount) {
    this.codTxnCount = codTxnCount;
  }
}