package com.hk.report.dto.inventory;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: May 22, 2012
 * Time: 6:18:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockReportDto {
  private String productVariant;
  private String productName;
  private String productOption;
  private Long openingStock;
  private Long lineItemCheckout;
  private Long reconcileCheckout;
  private Long grnCheckin;
  private Long reconcileCheckin;
  private Long rtoCheckin;
  private Long moveBackToActionQueueCheckin;
  private Long damageCheckin;
  private Long stockLeft;

  public String getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(String productVariant) {
    this.productVariant = productVariant;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductOption() {
    return productOption;
  }

  public void setProductOption(String productOption) {
    this.productOption = productOption;
  }

  public Long getOpeningStock() {
    return openingStock;
  }

  public void setOpeningStock(Long openingStock) {
    this.openingStock = openingStock;
  }

  public Long getLineItemCheckout() {
    return lineItemCheckout;
  }

  public void setLineItemCheckout(Long lineItemCheckout) {
    this.lineItemCheckout = lineItemCheckout;
  }

  public Long getReconcileCheckout() {
    return reconcileCheckout;
  }

  public void setReconcileCheckout(Long reconcileCheckout) {
    this.reconcileCheckout = reconcileCheckout;
  }

  public Long getGrnCheckin() {
    return grnCheckin;
  }

  public void setGrnCheckin(Long grnCheckin) {
    this.grnCheckin = grnCheckin;
  }

  public Long getReconcileCheckin() {
    return reconcileCheckin;
  }

  public void setReconcileCheckin(Long reconcileCheckin) {
    this.reconcileCheckin = reconcileCheckin;
  }

  public Long getRtoCheckin() {
    return rtoCheckin;
  }

  public void setRtoCheckin(Long rtoCheckin) {
    this.rtoCheckin = rtoCheckin;
  }

  public Long getMoveBackToActionQueueCheckin() {
    return moveBackToActionQueueCheckin;
  }

  public void setMoveBackToActionQueueCheckin(Long moveBackCheckin) {
    this.moveBackToActionQueueCheckin = moveBackCheckin;
  }

  public Long getDamageCheckin() {
    return damageCheckin;
  }

  public void setDamageCheckin(Long damageCheckin) {
    this.damageCheckin = damageCheckin;
  }

  public Long getStockLeft() {
    return stockLeft;
  }

  public void setStockLeft(Long stockLeft) {
    this.stockLeft = stockLeft;
  }
}
