package com.hk.report.dto.order.reconcilation;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Jul 4, 2011
 * Time: 3:00:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReconciliationDto {
  Long ORDER_ID;
  Date ORDER_DATE;
  int TOTAL_NO_OF_PRODUCTS;
  Double SUM_MRP;
  Double SUM_DISCOUNT;
  String PAYMENT_MODE;
  String COURIER;
  String PROCESSED_STATUS;
  String PAYMENT_STATUS;
  Date SHIPMENT_DATE;
  Date DELIVERY_DATE;

  public Long getORDER_ID() {
    return ORDER_ID;
  }

  public void setORDER_ID(Long ORDER_ID) {
    this.ORDER_ID = ORDER_ID;
  }

  public Date getORDER_DATE() {
    return ORDER_DATE;
  }

  public void setORDER_DATE(Date ORDER_DATE) {
    this.ORDER_DATE = ORDER_DATE;
  }

  public int getTOTAL_NO_OF_PRODUCTS() {
    return TOTAL_NO_OF_PRODUCTS;
  }

  public void setTOTAL_NO_OF_PRODUCTS(int TOTAL_NO_OF_PRODUCTS) {
    this.TOTAL_NO_OF_PRODUCTS = TOTAL_NO_OF_PRODUCTS;
  }

  public Double getSUM_MRP() {
    return SUM_MRP;
  }

  public void setSUM_MRP(Double SUM_MRP) {
    this.SUM_MRP = SUM_MRP;
  }

  public Double getSUM_DISCOUNT() {
    return SUM_DISCOUNT;
  }

  public void setSUM_DISCOUNT(Double SUM_DISCOUNT) {
    this.SUM_DISCOUNT = SUM_DISCOUNT;
  }

  public String getPAYMENT_MODE() {
    return PAYMENT_MODE;
  }

  public void setPAYMENT_MODE(String PAYMENT_MODE) {
    this.PAYMENT_MODE = PAYMENT_MODE;
  }

  public String getCOURIER() {
    return COURIER;
  }

  public void setCOURIER(String COURIER) {
    this.COURIER = COURIER;
  }

  public String getPROCESSED_STATUS() {
    return PROCESSED_STATUS;
  }

  public void setPROCESSED_STATUS(String PROCESSED_STATUS) {
    this.PROCESSED_STATUS = PROCESSED_STATUS;
  }

  public String getPAYMENT_STATUS() {
    return PAYMENT_STATUS;
  }

  public void setPAYMENT_STATUS(String PAYMENT_STATUS) {
    this.PAYMENT_STATUS = PAYMENT_STATUS;
  }

  public Date getSHIPMENT_DATE() {
    return SHIPMENT_DATE;
  }

  public void setSHIPMENT_DATE(Date SHIPMENT_DATE) {
    this.SHIPMENT_DATE = SHIPMENT_DATE;
  }

  public Date getDELIVERY_DATE() {
    return DELIVERY_DATE;
  }

  public void setDELIVERY_DATE(Date DELIVERY_DATE) {
    this.DELIVERY_DATE = DELIVERY_DATE;
  }
}
