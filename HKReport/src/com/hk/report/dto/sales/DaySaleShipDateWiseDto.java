package com.hk.report.dto.sales;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 15, 2011
 * Time: 12:49:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DaySaleShipDateWiseDto {

  Date orderDate;
  Date orderShipDate;
  Long txnCountOfShippedItems;
  Long skuCountOfShippedItems;
  Double sumOfMrpOfShippedItems;
  Double sumOfHkPriceOfShippedItems;
  Double sumOfAllDiscountsOfShippedItems;
  Double sumOfForwardingChargesOfShippedItems;
  Double sumOfHkPricePostAllDiscountsOfShippedItems;
  Double sumOfNetReceivableOfShippedItems;

  Long txnCountOfDeliveredItems;
  Long skuCountOfDeliveredItems;
  Double sumOfMrpOfDeliveredItems;
  Double sumOfHkPriceOfDeliveredItems;
  Double sumOfAllDiscountsOfDeliveredItems;
  Double sumOfForwardingChargesOfDeliveredItems;
  Double sumOfHkPricePostAllDiscountsOfDeliveredItems;
  Double sumOfNetReceivableOfDeliveredItems;


  Long txnCountOfReturnedItems;
  Long skuCountOfReturnedItems;
  Double sumOfMrpOfReturnedItems;
  Double sumOfHkPriceOfReturnedItems;
  Double sumOfAllDiscountsOfReturnedItems;
  Double sumOfForwardingChargesOfReturnedItems;
  Double sumOfHkPricePostAllDiscountsOfReturnedItems;
  Double sumOfNetReceivableOfReturnedItems;


  Long txnCountOfCancelledItems;
  Long skuCountOfCancelledItems;
  Double sumOfMrpOfCancelledItems;
  Double sumOfHkPriceOfCancelledItems;
  Double sumOfAllDiscountsOfCancelledItems;
  Double sumOfForwardingChargesOfCancelledItems;
  Double sumOfHkPricePostAllDiscountsOfCancelledItems;
  Double sumOfNetReceivableOfCancelledItems;


  Long totalTxnCount;
  Long totalSkuCount;
  Double totalSumOfMrp;
  Double totalSumOfHkPrice;
  Double totalSumOfAllDiscounts;
  Double totalSumOfForwardingCharges;
  Double totalSumOfHkPricePostAllDiscounts;
  Double totalSumOfNetReceivable;


  public DaySaleShipDateWiseDto() {

    txnCountOfShippedItems = 0L;
    skuCountOfShippedItems = 0L;
    sumOfMrpOfShippedItems = 0.0;
    sumOfHkPriceOfShippedItems = 0.0;
    sumOfAllDiscountsOfShippedItems = 0.0;
    sumOfForwardingChargesOfShippedItems = 0.0;
    sumOfHkPricePostAllDiscountsOfShippedItems = 0.0;
    sumOfNetReceivableOfShippedItems = 0.0;


    txnCountOfDeliveredItems = 0L;
    skuCountOfDeliveredItems = 0L;
    sumOfMrpOfDeliveredItems = 0.0;
    sumOfHkPriceOfDeliveredItems = 0.0;
    sumOfAllDiscountsOfDeliveredItems = 0.0;
    sumOfForwardingChargesOfDeliveredItems = 0.0;
    sumOfHkPricePostAllDiscountsOfDeliveredItems = 0.0;
    sumOfNetReceivableOfDeliveredItems = 0.0;


    txnCountOfReturnedItems = 0L;
    skuCountOfReturnedItems = 0L;
    sumOfMrpOfReturnedItems = 0.0;
    sumOfHkPriceOfReturnedItems = 0.0;
    sumOfAllDiscountsOfReturnedItems = 0.0;
    sumOfForwardingChargesOfReturnedItems = 0.0;
    sumOfHkPricePostAllDiscountsOfReturnedItems = 0.0;
    sumOfNetReceivableOfReturnedItems = 0.0;


    txnCountOfCancelledItems = 0L;
    skuCountOfCancelledItems = 0L;
    sumOfMrpOfCancelledItems = 0.0;
    sumOfHkPriceOfCancelledItems = 0.0;
    sumOfAllDiscountsOfCancelledItems = 0.0;
    sumOfForwardingChargesOfCancelledItems = 0.0;
    sumOfHkPricePostAllDiscountsOfCancelledItems = 0.0;
    sumOfNetReceivableOfCancelledItems = 0.0;


    totalTxnCount = 0L;
    totalSkuCount = 0L;
    totalSumOfMrp = 0.0;
    totalSumOfHkPrice = 0.0;
    totalSumOfAllDiscounts = 0.0;
    totalSumOfForwardingCharges = 0.0;
    totalSumOfHkPricePostAllDiscounts = 0.0;
    totalSumOfNetReceivable = 0.0;

  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public Date getOrderShipDate() {
    return orderShipDate;
  }

  public void setOrderShipDate(Date orderShipDate) {
    this.orderShipDate = orderShipDate;
  }

  public Long getTxnCountOfShippedItems() {
    return txnCountOfShippedItems;
  }

  public void setTxnCountOfShippedItems(Long txnCountOfShippedItems) {
    this.txnCountOfShippedItems = txnCountOfShippedItems;
  }

  public Long getSkuCountOfShippedItems() {
    return skuCountOfShippedItems;
  }

  public void setSkuCountOfShippedItems(Long skuCountOfShippedItems) {
    this.skuCountOfShippedItems = skuCountOfShippedItems;
  }

  public Double getSumOfMrpOfShippedItems() {
    return sumOfMrpOfShippedItems;
  }

  public void setSumOfMrpOfShippedItems(Double sumOfMrpOfShippedItems) {
    this.sumOfMrpOfShippedItems = sumOfMrpOfShippedItems;
  }

  public Double getSumOfHkPriceOfShippedItems() {
    return sumOfHkPriceOfShippedItems;
  }

  public void setSumOfHkPriceOfShippedItems(Double sumOfHkPriceOfShippedItems) {
    this.sumOfHkPriceOfShippedItems = sumOfHkPriceOfShippedItems;
  }

  public Double getSumOfHkPricePostAllDiscountsOfShippedItems() {
    return sumOfHkPricePostAllDiscountsOfShippedItems;
  }

  public void setSumOfHkPricePostAllDiscountsOfShippedItems(Double sumOfHkPricePostAllDiscountsOfShippedItems) {
    this.sumOfHkPricePostAllDiscountsOfShippedItems = sumOfHkPricePostAllDiscountsOfShippedItems;
  }

  public Long getTxnCountOfDeliveredItems() {
    return txnCountOfDeliveredItems;
  }

  public void setTxnCountOfDeliveredItems(Long txnCountOfDeliveredItems) {
    this.txnCountOfDeliveredItems = txnCountOfDeliveredItems;
  }

  public Long getSkuCountOfDeliveredItems() {
    return skuCountOfDeliveredItems;
  }

  public void setSkuCountOfDeliveredItems(Long skuCountOfDeliveredItems) {
    this.skuCountOfDeliveredItems = skuCountOfDeliveredItems;
  }

  public Double getSumOfMrpOfDeliveredItems() {
    return sumOfMrpOfDeliveredItems;
  }

  public void setSumOfMrpOfDeliveredItems(Double sumOfMrpOfDeliveredItems) {
    this.sumOfMrpOfDeliveredItems = sumOfMrpOfDeliveredItems;
  }

  public Double getSumOfHkPriceOfDeliveredItems() {
    return sumOfHkPriceOfDeliveredItems;
  }

  public void setSumOfHkPriceOfDeliveredItems(Double sumOfHkPriceOfDeliveredItems) {
    this.sumOfHkPriceOfDeliveredItems = sumOfHkPriceOfDeliveredItems;
  }

  public Double getSumOfHkPricePostAllDiscountsOfDeliveredItems() {
    return sumOfHkPricePostAllDiscountsOfDeliveredItems;
  }

  public void setSumOfHkPricePostAllDiscountsOfDeliveredItems(Double sumOfHkPricePostAllDiscountsOfDeliveredItems) {
    this.sumOfHkPricePostAllDiscountsOfDeliveredItems = sumOfHkPricePostAllDiscountsOfDeliveredItems;
  }

  public Long getTxnCountOfReturnedItems() {
    return txnCountOfReturnedItems;
  }

  public void setTxnCountOfReturnedItems(Long txnCountOfReturnedItems) {
    this.txnCountOfReturnedItems = txnCountOfReturnedItems;
  }

  public Long getSkuCountOfReturnedItems() {
    return skuCountOfReturnedItems;
  }

  public void setSkuCountOfReturnedItems(Long skuCountOfReturnedItems) {
    this.skuCountOfReturnedItems = skuCountOfReturnedItems;
  }

  public Double getSumOfMrpOfReturnedItems() {
    return sumOfMrpOfReturnedItems;
  }

  public void setSumOfMrpOfReturnedItems(Double sumOfMrpOfReturnedItems) {
    this.sumOfMrpOfReturnedItems = sumOfMrpOfReturnedItems;
  }

  public Double getSumOfHkPriceOfReturnedItems() {
    return sumOfHkPriceOfReturnedItems;
  }

  public void setSumOfHkPriceOfReturnedItems(Double sumOfHkPriceOfReturnedItems) {
    this.sumOfHkPriceOfReturnedItems = sumOfHkPriceOfReturnedItems;
  }

  public Double getSumOfHkPricePostAllDiscountsOfReturnedItems() {
    return sumOfHkPricePostAllDiscountsOfReturnedItems;
  }

  public void setSumOfHkPricePostAllDiscountsOfReturnedItems(Double sumOfHkPricePostAllDiscountsOfReturnedItems) {
    this.sumOfHkPricePostAllDiscountsOfReturnedItems = sumOfHkPricePostAllDiscountsOfReturnedItems;
  }

  public Long getTxnCountOfCancelledItems() {
    return txnCountOfCancelledItems;
  }

  public void setTxnCountOfCancelledItems(Long txnCountOfCancelledItems) {
    this.txnCountOfCancelledItems = txnCountOfCancelledItems;
  }

  public Long getSkuCountOfCancelledItems() {
    return skuCountOfCancelledItems;
  }

  public void setSkuCountOfCancelledItems(Long skuCountOfCancelledItems) {
    this.skuCountOfCancelledItems = skuCountOfCancelledItems;
  }

  public Double getSumOfMrpOfCancelledItems() {
    return sumOfMrpOfCancelledItems;
  }

  public void setSumOfMrpOfCancelledItems(Double sumOfMrpOfCancelledItems) {
    this.sumOfMrpOfCancelledItems = sumOfMrpOfCancelledItems;
  }

  public Double getSumOfHkPriceOfCancelledItems() {
    return sumOfHkPriceOfCancelledItems;
  }

  public void setSumOfHkPriceOfCancelledItems(Double sumOfHkPriceOfCancelledItems) {
    this.sumOfHkPriceOfCancelledItems = sumOfHkPriceOfCancelledItems;
  }

  public Double getSumOfHkPricePostAllDiscountsOfCancelledItems() {
    return sumOfHkPricePostAllDiscountsOfCancelledItems;
  }

  public void setSumOfHkPricePostAllDiscountsOfCancelledItems(Double sumOfHkPricePostAllDiscountsOfCancelledItems) {
    this.sumOfHkPricePostAllDiscountsOfCancelledItems = sumOfHkPricePostAllDiscountsOfCancelledItems;
  }

  public Long getTotalTxnCount() {
    return totalTxnCount;
  }

  public void setTotalTxnCount(Long totalTxnCount) {
    this.totalTxnCount = totalTxnCount;
  }

  public Long getTotalSkuCount() {
    return totalSkuCount;
  }

  public void setTotalSkuCount(Long totalSkuCount) {
    this.totalSkuCount = totalSkuCount;
  }

  public Double getTotalSumOfMrp() {
    return totalSumOfMrp;
  }

  public void setTotalSumOfMrp(Double totalSumOfMrp) {
    this.totalSumOfMrp = totalSumOfMrp;
  }

  public Double getTotalSumOfHkPrice() {
    return totalSumOfHkPrice;
  }

  public void setTotalSumOfHkPrice(Double totalSumOfHkPrice) {
    this.totalSumOfHkPrice = totalSumOfHkPrice;
  }

  public Double getTotalSumOfHkPricePostAllDiscounts() {
    return totalSumOfHkPricePostAllDiscounts;
  }

  public void setTotalSumOfHkPricePostAllDiscounts(Double totalSumOfHkPricePostAllDiscounts) {
    this.totalSumOfHkPricePostAllDiscounts = totalSumOfHkPricePostAllDiscounts;
  }

  public Double getSumOfAllDiscountsOfShippedItems() {
    return sumOfAllDiscountsOfShippedItems;
  }

  public void setSumOfAllDiscountsOfShippedItems(Double sumOfAllDiscountsOfShippedItems) {
    this.sumOfAllDiscountsOfShippedItems = sumOfAllDiscountsOfShippedItems;
  }

  public Double getSumOfForwardingChargesOfShippedItems() {
    return sumOfForwardingChargesOfShippedItems;
  }

  public void setSumOfForwardingChargesOfShippedItems(Double sumOfForwardingChargesOfShippedItems) {
    this.sumOfForwardingChargesOfShippedItems = sumOfForwardingChargesOfShippedItems;
  }

  public Double getSumOfAllDiscountsOfDeliveredItems() {
    return sumOfAllDiscountsOfDeliveredItems;
  }

  public void setSumOfAllDiscountsOfDeliveredItems(Double sumOfAllDiscountsOfDeliveredItems) {
    this.sumOfAllDiscountsOfDeliveredItems = sumOfAllDiscountsOfDeliveredItems;
  }

  public Double getSumOfForwardingChargesOfDeliveredItems() {
    return sumOfForwardingChargesOfDeliveredItems;
  }

  public void setSumOfForwardingChargesOfDeliveredItems(Double sumOfForwardingChargesOfDeliveredItems) {
    this.sumOfForwardingChargesOfDeliveredItems = sumOfForwardingChargesOfDeliveredItems;
  }

  public Double getSumOfAllDiscountsOfReturnedItems() {
    return sumOfAllDiscountsOfReturnedItems;
  }

  public void setSumOfAllDiscountsOfReturnedItems(Double sumOfAllDiscountsOfReturnedItems) {
    this.sumOfAllDiscountsOfReturnedItems = sumOfAllDiscountsOfReturnedItems;
  }

  public Double getSumOfForwardingChargesOfReturnedItems() {
    return sumOfForwardingChargesOfReturnedItems;
  }

  public void setSumOfForwardingChargesOfReturnedItems(Double sumOfForwardingChargesOfReturnedItems) {
    this.sumOfForwardingChargesOfReturnedItems = sumOfForwardingChargesOfReturnedItems;
  }

  public Double getSumOfAllDiscountsOfCancelledItems() {
    return sumOfAllDiscountsOfCancelledItems;
  }

  public void setSumOfAllDiscountsOfCancelledItems(Double sumOfAllDiscountsOfCancelledItems) {
    this.sumOfAllDiscountsOfCancelledItems = sumOfAllDiscountsOfCancelledItems;
  }

  public Double getSumOfForwardingChargesOfCancelledItems() {
    return sumOfForwardingChargesOfCancelledItems;
  }

  public void setSumOfForwardingChargesOfCancelledItems(Double sumOfForwardingChargesOfCancelledItems) {
    this.sumOfForwardingChargesOfCancelledItems = sumOfForwardingChargesOfCancelledItems;
  }

  public Double getTotalSumOfAllDiscounts() {
    return totalSumOfAllDiscounts;
  }

  public void setTotalSumOfAllDiscounts(Double totalSumOfAllDiscounts) {
    this.totalSumOfAllDiscounts = totalSumOfAllDiscounts;
  }

  public Double getTotalSumOfForwardingCharges() {
    return totalSumOfForwardingCharges;
  }

  public void setTotalSumOfForwardingCharges(Double totalSumOfForwardingCharges) {
    this.totalSumOfForwardingCharges = totalSumOfForwardingCharges;
  }

  public Double getSumOfNetReceivableOfShippedItems() {
    return sumOfNetReceivableOfShippedItems;
  }

  public void setSumOfNetReceivableOfShippedItems(Double sumOfNetReceivableOfShippedItems) {
    this.sumOfNetReceivableOfShippedItems = sumOfNetReceivableOfShippedItems;
  }

  public Double getSumOfNetReceivableOfDeliveredItems() {
    return sumOfNetReceivableOfDeliveredItems;
  }

  public void setSumOfNetReceivableOfDeliveredItems(Double sumOfNetReceivableOfDeliveredItems) {
    this.sumOfNetReceivableOfDeliveredItems = sumOfNetReceivableOfDeliveredItems;
  }

  public Double getSumOfNetReceivableOfReturnedItems() {
    return sumOfNetReceivableOfReturnedItems;
  }

  public void setSumOfNetReceivableOfReturnedItems(Double sumOfNetReceivableOfReturnedItems) {
    this.sumOfNetReceivableOfReturnedItems = sumOfNetReceivableOfReturnedItems;
  }

  public Double getSumOfNetReceivableOfCancelledItems() {
    return sumOfNetReceivableOfCancelledItems;
  }

  public void setSumOfNetReceivableOfCancelledItems(Double sumOfNetReceivableOfCancelledItems) {
    this.sumOfNetReceivableOfCancelledItems = sumOfNetReceivableOfCancelledItems;
  }

  public Double getTotalSumOfNetReceivable() {
    return totalSumOfNetReceivable;
  }

  public void setTotalSumOfNetReceivable(Double totalSumOfNetReceivable) {
    this.totalSumOfNetReceivable = totalSumOfNetReceivable;
  }
}
