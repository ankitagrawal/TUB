package com.hk.api.models.order;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 2, 2012
 * Time: 4:52:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class APIProductDetail {
  private String productId;
  private String qty;
  private Double storePrice;

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

  public Double getStorePrice() {
    return storePrice;
  }

  public void setStorePrice(Double storePrice) {
    this.storePrice = storePrice;
  }
}

