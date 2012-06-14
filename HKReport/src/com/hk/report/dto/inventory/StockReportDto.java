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
  private Long damageCheckin;
  private Long stockLeft;
  private Long inventoryQty;
  private Long inventoryTxnType;
  private Long cancelCheckin;
  private Long inventoryRepeatCheckout;
  private Long rvExpired;
  private Long rvLostPilferage;
  private Long stockTransferCheckin;
  private Long stockTransferCheckout;
  private Long transitLost;

  public StockReportDto() {
   openingStock = 0L;
   lineItemCheckout = 0L;
   reconcileCheckout = 0L;
   grnCheckin = 0L;
   reconcileCheckin = 0L;
   rtoCheckin = 0L;
   damageCheckin = 0L;
   stockLeft = 0L;
   inventoryQty = 0L;
   inventoryTxnType = 0L;
   cancelCheckin = 0L;
   inventoryRepeatCheckout = 0L;
   rvExpired = 0L;
   rvLostPilferage = 0L;
   stockTransferCheckin = 0L;
   stockTransferCheckout = 0L;
   transitLost = 0L;
  }

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

  public Long getInventoryQty() {
    return inventoryQty;
  }

  public void setInventoryQty(Long inventoryQty) {
    this.inventoryQty = inventoryQty;
  }

  public Long getInventoryTxnType() {
    return inventoryTxnType;
  }

  public void setInventoryTxnType(Long inventoryTxnType) {
    this.inventoryTxnType = inventoryTxnType;
  }

  public Long getCancelCheckin() {
    return cancelCheckin;
  }

  public void setCancelCheckin(Long cancelCheckin) {
    this.cancelCheckin = cancelCheckin;
  }

  public Long getInventoryRepeatCheckout() {
    return inventoryRepeatCheckout;
  }

  public void setInventoryRepeatCheckout(Long inventoryRepeatCheckout) {
    this.inventoryRepeatCheckout = inventoryRepeatCheckout;
  }

  public Long getRvExpired() {
    return rvExpired;
  }

  public void setRvExpired(Long rvExpired) {
    this.rvExpired = rvExpired;
  }

  public Long getRvLostPilferage() {
    return rvLostPilferage;
  }

  public void setRvLostPilferage(Long rvLostPilferage) {
    this.rvLostPilferage = rvLostPilferage;
  }

  public Long getStockTransferCheckin() {
    return stockTransferCheckin;
  }

  public void setStockTransferCheckin(Long stockTransferCheckin) {
    this.stockTransferCheckin = stockTransferCheckin;
  }

  public Long getStockTransferCheckout() {
    return stockTransferCheckout;
  }

  public void setStockTransferCheckout(Long stockTransferCheckout) {
    this.stockTransferCheckout = stockTransferCheckout;
  }

  public Long getTransitLost() {
    return transitLost;
  }

  public void setTransitLost(Long transitLost) {
    this.transitLost = transitLost;
  }
}
