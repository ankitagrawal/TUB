package com.hk.exception;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.warehouse.Warehouse;

public class NoSkuException extends HealthkartRuntimeException {

  ProductVariant productVariant;
  Warehouse warehouse;

  public NoSkuException(String message, ProductVariant productVariant, Warehouse warehouse) {
    super(message);
    this.productVariant = productVariant;
    this.warehouse = warehouse;
  }

  public NoSkuException(ProductVariant productVariant, Warehouse warehouse) {
    super("No sku for product variant(" + productVariant.getId() + ") and warehouse(" + warehouse.getCity() + ").");
    this.productVariant = productVariant;
    this.warehouse = warehouse;
  }

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }
}