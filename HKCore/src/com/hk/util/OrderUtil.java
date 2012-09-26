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
                productVariants.add(lineItem.getSku().getProductVariant());
            }
            return getDispatchDaysForVariants(productVariants);
        } else {
            return new Long[] { DEFAULT_MIN_DEL_DAYS, DEFAULT_MIN_DEL_DAYS };
        }
    }

    public static Long[] getDispatchDaysForBO(Order order) {
        if (order != null) {
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            Set<ProductVariant> productVariants = new HashSet<ProductVariant>();

            for (CartLineItem cartLineItem : productCartLineItems) {
                productVariants.add(cartLineItem.getProductVariant());
            }

            return getDispatchDaysForVariants(productVariants);
        } else {
            return new Long[] { DEFAULT_MIN_DEL_DAYS, DEFAULT_MIN_DEL_DAYS };
        }
    }

    /**
     * returns min/max dispatch days for set of product variants, [0] will be min days and [1] will be maax days
     * 
     * @param productVariants
     * @return
     */
    public static Long[] getDispatchDaysForVariants(Set<ProductVariant> productVariants) {

        long minDays = DEFAULT_MIN_DEL_DAYS, maxDays = DEFAULT_MIN_DEL_DAYS;

        for (ProductVariant productVariant : productVariants) {
            if (productVariant != null) {
                Product product = productVariant.getProduct();
                if (product.getMinDays() != null && product.getMinDays() > minDays) {
                    minDays = product.getMinDays();
                }
                if (product.getMaxDays() != null && product.getMaxDays() > maxDays) {
                    maxDays = product.getMaxDays();
                }
            }
        }

        return new Long[] { minDays, maxDays };
    }
}
