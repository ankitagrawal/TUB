package com.hk.rest.models.order;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 2, 2012
 * Time: 4:52:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIProductDetail {
  private String productId;
  private String qty;
  private Double mihPrice;

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getQty() {
    return qty;
  }

  public void setQty(String qty) {
    this.qty = qty;
  }

  public Double getMihPrice() {
    return mihPrice;
  }

  public void setMihPrice(Double mihPrice) {
    this.mihPrice = mihPrice;
  }
}

