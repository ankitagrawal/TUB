package com.hk.constants.catalog.product;

import com.hk.domain.core.ProductVariantServiceType;


/**
 * Generated
 */

public enum EnumProductVariantServiceType {
  Merchant2Home(10L, "Merchant2Home"),
  Home2Merchant(20L, "Home2Merchant"),
  BothWays(30L, "BothWays"),
  Default(99L, "Default"),;

  private java.lang.String name;
  private java.lang.Long id;

  EnumProductVariantServiceType(Long id, java.lang.String name) {
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

  public ProductVariantServiceType asProductVariantServiceType() {
    ProductVariantServiceType productVariantServiceType = new ProductVariantServiceType();
    productVariantServiceType.setId(this.getId());
    productVariantServiceType.setName(this.getName());
    return productVariantServiceType;
  }

}



