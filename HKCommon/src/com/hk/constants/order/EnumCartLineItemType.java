package com.hk.constants.order;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.hk.domain.core.CartLineItemType;



public enum EnumCartLineItemType {

  Product(10L, "Product"),
  Shipping(20L, "Shipping"),
  OrderLevelDiscount(30L, "OrderLevelDiscount"),
  CodCharges(40L, "COD Charges"),
  RewardPoint(50L, "Reward Point"),
  Subscription(70L, "Subscription"),
  ;

  private java.lang.String name;
  private java.lang.Long id;

  EnumCartLineItemType(java.lang.Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public static List<Long> getCartLineItemTypeIDs(Set<EnumCartLineItemType> lineItemTypes) {
    List<Long> lineItemTypeIDs = new ArrayList<Long>();
    for (EnumCartLineItemType lineItemType : lineItemTypes) {
      lineItemTypeIDs.add(lineItemType.getId());
    }
    return lineItemTypeIDs;
  }

  public CartLineItemType asCartLineItemType() {
    CartLineItemType lineItemType = new CartLineItemType();
    lineItemType.setId(id);
    lineItemType.setName(name);
    return lineItemType;
  }
}

