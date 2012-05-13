package com.hk.rest.models.order;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 2, 2012
 * Time: 4:52:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIPayment {
   private String paymentmodeId;
   private String bankId;
   private String status;

  public String getPaymentmodeId() {
    return paymentmodeId;
  }

  public void setPaymentmodeId(String paymentmodeId) {
    this.paymentmodeId = paymentmodeId;
  }

  public String getBankId() {
    return bankId;
  }

  public void setBankId(String bankId) {
    this.bankId = bankId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
