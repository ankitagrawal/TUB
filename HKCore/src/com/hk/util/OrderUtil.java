package com.hk.util;

import java.util.HashSet;
import java.util.Set;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

public class OrderUtil {

    public static final long DEFAULT_MIN_DEL_DAYS = 1;
    public static final long DEFAULT_MAX_DEL_DAYS = 3;

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

    public static Long[] getDispatchDaysForSO(ShippingOrder shippingOrder) {
        if (shippingOrder != null) {
            Set<ProductVariant> productVariants = new HashSet<ProductVariant>();
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                //todo eyeglasses bug fix
//              if (lineItem.getCartLineItem().getCartLineItemConfig()!=null) {
//                lineItem.getSku().getProductVariant().getProduct().setJit(true);
//              }
              productVariants.add(lineItem.getSku().getProductVariant());
            }
            return getDispatchDaysForVariants(productVariants);
        } else {
            return new Long[] { DEFAULT_MIN_DEL_DAYS, DEFAULT_MAX_DEL_DAYS };
        }
    }
    
    

    public static Long[] getDispatchDaysForBO(Order order) {
        if (order != null) {
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            Set<ProductVariant> productVariants = new HashSet<ProductVariant>();

            for (CartLineItem cartLineItem : productCartLineItems) {
//              if (cartLineItem.getCartLineItemConfig() != null) {
//                cartLineItem.getProductVariant().getProduct().setJit(true);
//              }
                productVariants.add(cartLineItem.getProductVariant());
            }

            return getDispatchDaysForVariants(productVariants);
        } else {
            return new Long[] { DEFAULT_MIN_DEL_DAYS, DEFAULT_MAX_DEL_DAYS };
        }
    }

    /**
     * returns min/max dispatch days for set of product variants, [0] will be min days and [1] will be max days
     * 
     * @param productVariants
     * @return
     */
    public static Long[] getDispatchDaysForVariants(Set<ProductVariant> productVariants) {

        long minDays = 1, maxDays = 3;
        long defaultMinDays = DEFAULT_MIN_DEL_DAYS, defaultMaxDays = DEFAULT_MAX_DEL_DAYS;

        for (ProductVariant productVariant : productVariants) {
            if (productVariant != null) {
                Product product = productVariant.getProduct();
                long productMinDays = product.isJit() ? product.getMinDays() : defaultMinDays;
                if (product.getMinDays() != null && productMinDays > minDays) {
                    minDays = product.getMinDays();
                }
                long productMaxDays = product.isJit() ? product.getMaxDays() : defaultMaxDays;
                if (product.getMaxDays() != null && productMaxDays > maxDays) {
                    maxDays = product.getMaxDays();
                }
            }
        }

        return new Long[] { minDays, maxDays };
    }
}
