package com.hk.report.dto.order;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItemExtraOption;

public class ProductLineItemWithExtraOptionsDto {

  ProductVariant productVariant;
  List<CartLineItemExtraOption> extraOptions;
  boolean selected;

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public List<CartLineItemExtraOption> getExtraOptions() {
    return extraOptions;
  }

  public void setExtraOptions(List<CartLineItemExtraOption> extraOptions) {
    this.extraOptions = extraOptions;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
