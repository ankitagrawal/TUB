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
