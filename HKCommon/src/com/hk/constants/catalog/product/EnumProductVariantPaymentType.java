package com.hk.constants.catalog.product;

import com.hk.domain.core.ProductVariantPaymentType;


/**
 * Generated
 */

public enum EnumProductVariantPaymentType {
  Prepaid(10L, "Prepaid"),
  Postpaid(20L, "Postpaid"),
  Default(99L, "Default"),;

  private java.lang.String name;
  private java.lang.Long id;

  EnumProductVariantPaymentType(Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductVariantPaymentType asProductVariantPaymentType() {
    ProductVariantPaymentType productVariantPaymentType = new ProductVariantPaymentType();
    productVariantPaymentType.setId(this.getId());
    productVariantPaymentType.setName(this.getName());
    return productVariantPaymentType;
  }

}



