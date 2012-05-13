package com.hk.util;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;


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
