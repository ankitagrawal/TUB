package com.hk.report.dto.inventory;

import com.hk.domain.sku.SkuGroup;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: May 16, 2012
 * Time: 8:44:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpiryAlertReportDto {
    private String batchNumber;
    private String productVariantId;
    private String productName;
    private String productOption;
    private SkuGroup skuGroup;
    private Long batchQty;

    public String getBatchNumber() {
      return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
      this.batchNumber = batchNumber;
    }

    public String getProductVariantId() {
      return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
      this.productVariantId = productVariantId;
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

    public SkuGroup getSkuGroup() {
      return skuGroup;
    }

    public void setSkuGroup(SkuGroup skuGroup) {
      this.skuGroup = skuGroup;
    }

    public Long getBatchQty() {
      return batchQty;
    }

    public void setBatchQty(Long batchQty) {
      this.batchQty = batchQty;
    }
}
