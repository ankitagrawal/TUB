package com.hk.report.dto.catalog;

import com.hk.domain.catalog.product.Product;


public class ComboProductAndAllowedVariantsDto {

  Long id;
  Product product;
  Long qty;
  String allowedVariants;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public String getAllowedVariants() {
    return allowedVariants;
  }

  public void setAllowedVariants(String allowedVariants) {
    this.allowedVariants = allowedVariants;
  }
}