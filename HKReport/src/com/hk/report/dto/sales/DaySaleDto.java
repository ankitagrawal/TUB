package com.hk.report.dto.sales;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.Tax;


public class DaySaleDto {

  Date orderDate;
  Date orderShipDate;
  OrderStatus orderStatus;
  Long txnCount;
  Long firstTxnCount;
  Long codTxnCount;
  Long offerTxnCount;
  Long skuCount;
  Double sumOfMrp;
  Double sumOfHkPrice;
  Double sumOfAllDiscounts;
  Double sumOfHkPricePostAllDiscounts;
  Double sumOfForwardingCharges;

  Long shippedSameDay;
  Long shippedNextDay;
  Long shippedOnDayTwo;

  String saleInState;
  Tax taxCategory;

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public Long getTxnCount() {
    if (txnCount == null) {
      return 0L;
    }
    return txnCount;
  }

  public void setTxnCount(Long txnCount) {
    this.txnCount = txnCount;
  }

  public Long getFirstTxnCount() {
    if (firstTxnCount == null) {
      return 0L;
    }
    return firstTxnCount;
  }

  public void setFirstTxnCount(Long firstTxnCount) {
    this.firstTxnCount = firstTxnCount;
  }

  public Long getSkuCount() {
    if (skuCount == null) {
      return 0L;
    }
    return skuCount;
  }

  public void setSkuCount(Long skuCount) {
    this.skuCount = skuCount;
  }

  public Double getSumOfMrp() {
    if (sumOfMrp == null) {
      return 0.0;
    }
    return sumOfMrp;
  }

  public void setSumOfMrp(Double sumOfMrp) {
    this.sumOfMrp = sumOfMrp;
  }

  public Double getSumOfHkPrice() {
    if (sumOfHkPrice == null) {
      return 0.0;
    }
    return sumOfHkPrice;
  }

  public void setSumOfHkPrice(Double sumOfHkPrice) {
    this.sumOfHkPrice = sumOfHkPrice;
  }

  public Double getSumOfHkPricePostAllDiscounts() {
    if (sumOfHkPricePostAllDiscounts == null) {
      return 0.0;
    }
    return sumOfHkPricePostAllDiscounts;
  }

  public void setSumOfHkPricePostAllDiscounts(Double sumOfHkPricePostAllDiscounts) {
    this.sumOfHkPricePostAllDiscounts = sumOfHkPricePostAllDiscounts;
  }

  public Long getShippedSameDay() {
    if (shippedSameDay == null) {
      return 0L;
    }
    return shippedSameDay;
  }

  public void setShippedSameDay(Long shippedSameDay) {
    this.shippedSameDay = shippedSameDay;
  }

  public Long getShippedNextDay() {
    if (shippedNextDay == null) {
      return 0L;
    }
    return shippedNextDay;
  }

  public void setShippedNextDay(Long shippedNextDay) {
    this.shippedNextDay = shippedNextDay;
  }

  public Long getShippedOnDayTwo() {
    if (shippedOnDayTwo == null) {
      return 0L;
    }
    return shippedOnDayTwo;
  }

  public void setShippedOnDayTwo(Long shippedOnDayTwo) {
    this.shippedOnDayTwo = shippedOnDayTwo;
  }

  public Long getCodTxnCount() {
    if (codTxnCount == null) {
      return 0L;
    }
    return codTxnCount;
  }

  public void setCodTxnCount(Long codTxnCount) {
    this.codTxnCount = codTxnCount;
  }

  public Long getOfferTxnCount() {
    if (offerTxnCount == null) {
      return 0L;
    }
    return offerTxnCount;
  }

  public void setOfferTxnCount(Long offerTxnCount) {
    this.offerTxnCount = offerTxnCount;
  }

  public Date getOrderShipDate() {
    return orderShipDate;
  }

  public void setOrderShipDate(Date orderShipDate) {
    this.orderShipDate = orderShipDate;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Double getSumOfAllDiscounts() {
    if (sumOfAllDiscounts == null) {
      return 0.0;
    }
    return sumOfAllDiscounts;
  }

  public void setSumOfAllDiscounts(Double sumOfAllDiscounts) {
    this.sumOfAllDiscounts = sumOfAllDiscounts;
  }

  public Double getSumOfForwardingCharges() {
    if (sumOfForwardingCharges == null) {
      return 0.0;
    }
    return sumOfForwardingCharges;
  }

  public void setSumOfForwardingCharges(Double sumOfForwardingCharges) {
    this.sumOfForwardingCharges = sumOfForwardingCharges;
  }

  public String getSaleInState() {
    return saleInState;
  }

  public void setSaleInState(String saleInState) {
    this.saleInState = saleInState;
  }

  public Tax getTaxCategory() {
    return taxCategory;
  }

  public void setTaxCategory(Tax taxCategory) {
    this.taxCategory = taxCategory;
  }


  //using this only for ease in report generation, may be we can use a dictionay pattern for such objects in future in sync with a XLS generator .

  public Object get(int colIdx) {
    switch (colIdx) {
      case 0:
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(orderShipDate);
      case 1:
        return Math.round(getSumOfMrp());
      case 2:
        return Math.round(getSumOfHkPrice());
      case 3:
        return Math.round(getSumOfAllDiscounts());
      case 4:
        return Math.round(getSumOfHkPrice() - getSumOfAllDiscounts());
      case 5:
        return Math.round(getSumOfForwardingCharges());
      case 6:
        return Math.round(getSumOfHkPrice() - getSumOfAllDiscounts() + getSumOfForwardingCharges());
      default:
        return "NA";
    }
  }
}