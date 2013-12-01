package com.hk.report.dto.inventory;

import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.inventory.rv.ReconciliationVoucher;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: Jun 22, 2012
 * Time: 6:00:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class RVReportDto {
  private Long qtyRV;
  private ReconciliationVoucher reconciliationVoucher;
  private ProductVariantInventory productVariantInventory;

  public Long getQtyRV() {
    return qtyRV;
  }

  public void setQtyRV(Long qtyRV) {
    this.qtyRV = qtyRV;
  }

  public ReconciliationVoucher getReconciliationVoucher() {
    return reconciliationVoucher;
  }

  public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
    this.reconciliationVoucher = reconciliationVoucher;
  }

  public ProductVariantInventory getProductVariantInventory() {
    return productVariantInventory;
  }

  public void setProductVariantInventory(ProductVariantInventory productVariantInventory) {
    this.productVariantInventory = productVariantInventory;
  }
}
