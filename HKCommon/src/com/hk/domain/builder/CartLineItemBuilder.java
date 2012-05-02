package com.hk.domain.builder;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;

public class CartLineItemBuilder {

  private EnumCartLineItemType enumCartLineItemType;
  private ProductVariant productVariant;
  private Long qty;
  //private Double costPrice;
  private Double markedPrice;
  private Double hkPrice;
  private Double discountOnHkPrice;
  //private Tax tax;

  public CartLineItemBuilder ofType(EnumCartLineItemType enumLineItemType) {
    this.enumCartLineItemType = enumLineItemType;
    return this;
  }

  public CartLineItemBuilder forVariantQty(ProductVariant productVariant, Long qty) {
    this.productVariant = productVariant;
    this.qty = qty;
    return this;
  }

  /*public CartLineItemBuilder costPrice(Double costPrice) {
    this.costPrice = costPrice;
    return this;
  }*/

  public CartLineItemBuilder hkPrice(Double hkPrice) {
    this.hkPrice = hkPrice;
    return this;
  }

  public CartLineItemBuilder markedPrice(Double markedPrice) {
    this.markedPrice = markedPrice;
    return this;
  }

  public CartLineItemBuilder discountOnHkPrice(Double discount) {
    this.discountOnHkPrice = discount;
    return this;
  }

  /*public CartLineItemBuilder tax(Tax tax) {
    this.tax = tax;
    return this;
  }*/

  public CartLineItem build() {
   // Tax tax = null;
    assert enumCartLineItemType != null;
    if (enumCartLineItemType == EnumCartLineItemType.Product) {
      assert productVariant != null;
      assert qty != null;
      //tax = productVariant.getTax();
    } else if (enumCartLineItemType == EnumCartLineItemType.Shipping) {
      //assert this.tax != null;
      //qty = PricingEngine.shippingDefaultQty;
        //TODO:rewrite
        qty = 1L;
      //tax = this.tax;
    } else if (enumCartLineItemType == EnumCartLineItemType.OrderLevelDiscount) {
      assert productVariant != null;
      assert discountOnHkPrice != null;
      hkPrice = 0D;
    } else if (enumCartLineItemType == EnumCartLineItemType.CodCharges) {
      //assert this.tax != null;
      //tax = this.tax;
//      qty = PricingEngine.shippingDefaultQty;
        //TODO:rewrite
        qty = 1L;

    } else if (enumCartLineItemType == EnumCartLineItemType.RewardPoint) {
      assert discountOnHkPrice != null;
      hkPrice = 0D;
      qty = 1L;
    }
    assert hkPrice != null;
    if (discountOnHkPrice == null) discountOnHkPrice = 0.0D;
    if (markedPrice == null) markedPrice = hkPrice;
    //if (costPrice == null) costPrice = hkPrice;
    //return new CartLineItem(enumLineItemType.asCartLineItemType(), productVariant, qty, costPrice, markedPrice, hkPrice, discountOnHkPrice, tax);
    return new CartLineItem(enumCartLineItemType.asCartLineItemType(), productVariant, qty, markedPrice, hkPrice, discountOnHkPrice);
  }
}
