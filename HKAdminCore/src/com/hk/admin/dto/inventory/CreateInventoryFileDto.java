package com.hk.admin.dto.inventory;

import java.util.Date;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;

public class CreateInventoryFileDto {
    String barcode;
    String name;
    Date expiryDate;
    Long sumQty;
    Double markedPrice;
	String batchNumber;
  //  String productVariantId;
    SkuGroup skuGroup;
    Product product;
    ProductVariantInventory productVariantInventory;
    ProductVariant productVariant;
    SkuItem skuItem;
    
    public String getBarcode() {
      return barcode;
    }

    public String getName() {
      return name;
    }

    public Date getExpiryDate() {
      return expiryDate;
    }

    public Long getSumQty() {
      return sumQty;
    }

    public Double getMarkedPrice() {
      return markedPrice;
    }

    public SkuGroup getSkuGroup() {
      return skuGroup;
    }

    public Product getProduct() {
      return product;
    }

    public ProductVariantInventory getProductVariantInventory() {
      return productVariantInventory;
    }

    public ProductVariant getProductVariant() {
      return productVariant;
    }

    public void setBarcode(String barcode) {
      this.barcode = barcode;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setExpiryDate(Date expiryDate) {
      this.expiryDate = expiryDate;
    }

    public void setSumQty(Long sumQty) {
      this.sumQty = sumQty;
    }

    public void setMarkedPrice(Double markedPrice) {
      this.markedPrice = markedPrice;
    }

    public void setSkuGroup(SkuGroup skuGroup) {
      this.skuGroup = skuGroup;
    }

    public void setProduct(Product product) {
      this.product = product;
    }

    public void setProductVariantInventory(ProductVariantInventory productVariantInventory) {
      this.productVariantInventory = productVariantInventory;
    }

    public void setProductVariant(ProductVariant productVariant) {
      this.productVariant = productVariant;
    }

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

    public SkuItem getSkuItem() {
        return skuItem;
    }

    public void setSkuItem(SkuItem skuItem) {
        this.skuItem = skuItem;
    }
}
