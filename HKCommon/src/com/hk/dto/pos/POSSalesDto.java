package com.hk.dto.pos;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/18/13
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class POSSalesDto {
  private Double amount;
  private String paymentMode;
  private Set cartLineItems;
  private Double discount;
  private Double margin;

  public Set getCartLineItems() {
    return cartLineItems;
  }

  public void setCartLineItems(Set cartLineItems) {
    this.cartLineItems = cartLineItems;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public Double getMargin() {
    return margin;
  }

  public void setMargin(Double margin) {
    this.margin = margin;
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
}
