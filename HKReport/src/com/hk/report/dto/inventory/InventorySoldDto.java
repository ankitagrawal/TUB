package com.hk.report.dto.inventory;


/**
 * Created by IntelliJ IDEA.
 * User: meenal
 * Date: Feb 28, 2012
 * Time: 5:38:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class InventorySoldDto {
  String productId;
  String productName;
  Long countSold;

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Long getCountSold() {
    return countSold;
  }

  public void setCountSold(Long countSold) {
    this.countSold = countSold;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }
}
