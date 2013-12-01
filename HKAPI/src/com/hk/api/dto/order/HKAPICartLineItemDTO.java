package com.hk.api.dto.order;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 */
public class HKAPICartLineItemDTO {
  private String productId;
  private Long qty;
  private Long cartLineItemType;
  private Double storePrice;
  private Double discountOnStorePrice;
  private Double storeMrp;

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public Double getStorePrice() {
    return storePrice;
  }

  public void setStorePrice(Double storePrice) {
    this.storePrice = storePrice;
  }

    public Double getDiscountOnStorePrice() {
        return discountOnStorePrice;
    }

    public void setDiscountOnStorePrice(Double discountOnStorePrice) {
        this.discountOnStorePrice = discountOnStorePrice;
    }

    public Double getStoreMrp() {
        return storeMrp;
    }

    public void setStoreMrp(Double storeMrp) {
        this.storeMrp = storeMrp;
    }

    public Long getCartLineItemType() {
        return cartLineItemType;
    }

    public void setCartLineItemType(Long cartLineItemType) {
        this.cartLineItemType = cartLineItemType;
    }
}

