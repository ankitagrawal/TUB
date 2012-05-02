package com.hk.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.comparator.BasketCategory;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.filter.CartLineItemFilter;


public class OrderUtil {

  public static Set<OrderCategory> getCategoriesForBaseOrder(Order order) {

   // Map<BasketCategory, Category> basketCategoryMap = new HashMap<BasketCategory, Category>();

    List<BasketCategory> basketCategories = new ArrayList<BasketCategory>();

    Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
    for (CartLineItem lineItem : cartLineItems) {
      Category lineItemPrimaryCategory = lineItem.getProductVariant().getProduct().getPrimaryCategory();
      BasketCategory lineItemBasketCategory = new BasketCategory(lineItemPrimaryCategory);

      if (basketCategories.contains(lineItemBasketCategory)) {
        BasketCategory basketCategoryToUpdate = basketCategories.get(basketCategories.indexOf(lineItemBasketCategory));
        basketCategoryToUpdate.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
      } else {
        lineItemBasketCategory.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
        basketCategories.add(lineItemBasketCategory);
      }

    }

    Collections.sort(basketCategories);

    Set<OrderCategory> orderCategories = new HashSet<OrderCategory>();
    boolean primaryCategory = true;

    for (BasketCategory basketCategory : basketCategories) {
      OrderCategory orderCategory = new OrderCategory();
      orderCategory.setOrder(order);
      orderCategory.setCategory(basketCategory.getCategory());
      if (primaryCategory) {
        orderCategory.setPrimary(true);
        primaryCategory = false;
      }
      orderCategories.add(orderCategory);
    }

    return orderCategories;

  }


  public static Category getBasketCategory(ShippingOrder shippingOrder) {
   List<BasketCategory> basketCategories = new ArrayList<BasketCategory>();

    for (LineItem lineItem : shippingOrder.getLineItems()) {
      Category lineItemPrimaryCategory = lineItem.getSku().getProductVariant().getProduct().getPrimaryCategory();
      BasketCategory lineItemBasketCategory = new BasketCategory(lineItemPrimaryCategory);

      if (basketCategories.contains(lineItemBasketCategory)) {
        BasketCategory basketCategoryToUpdate = basketCategories.get(basketCategories.indexOf(lineItemBasketCategory));
        basketCategoryToUpdate.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
      } else {
        lineItemBasketCategory.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
        basketCategories.add(lineItemBasketCategory);
      }

    }

    Collections.sort(basketCategories);

    LineItem firstLineItem = shippingOrder.getLineItems().iterator().next();
    Category basketCategory = firstLineItem.getSku().getProductVariant().getProduct().getPrimaryCategory();

    if (!basketCategories.isEmpty()) {
      basketCategory = basketCategories.get(0).getCategory();
    }

    return basketCategory;
  }


  public static void roundOffPricesOnCartLineItem(CartLineItem cartLineItem) {
    Double roundedOffHkPrice = NumberUtil.roundOff(cartLineItem.getHkPrice());
    cartLineItem.setHkPrice(roundedOffHkPrice);
    Double roundedOffDiscount = NumberUtil.roundOff(cartLineItem.getDiscountOnHkPrice());
    cartLineItem.setDiscountOnHkPrice(roundedOffDiscount);
  }

  public static void roundOffPricesOnLineItem(LineItem lineItem) {

    Double roundedOffHkPrice = NumberUtil.roundOff(lineItem.getHkPrice());
    lineItem.setHkPrice(roundedOffHkPrice);
    Double roundedOffDiscount = NumberUtil.roundOff(lineItem.getDiscountOnHkPrice());
    lineItem.setDiscountOnHkPrice(roundedOffDiscount);
  }

}
