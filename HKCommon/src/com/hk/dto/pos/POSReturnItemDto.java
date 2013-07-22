package com.hk.dto.pos;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/19/13
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class POSReturnItemDto {
  private Double amount;
  private String orderStatus;
  private String paymentMode;
  private String pvName;

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
  }

  public String getPvName() {
    return pvName;
  }

  public void setPvName(String pvName) {
    this.pvName = pvName;
  }
}
