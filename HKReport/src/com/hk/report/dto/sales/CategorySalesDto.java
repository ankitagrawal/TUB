package com.hk.report.dto.sales;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;


public class CategorySalesDto {

  private ProductVariant productVariant;

  private Category topLevelCategory;

  private Long qty;

  private String variantDetails;

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public Category getTopLevelCategory() {
    return topLevelCategory;
  }

  public void setTopLevelCategory(Category topLevelCategory) {
    this.topLevelCategory = topLevelCategory;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public String getVariantDetails() {
    return variantDetails;
  }

  public void setVariantDetails(String variantDetails) {
    this.variantDetails = variantDetails;
  }
}
