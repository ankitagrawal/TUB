package com.hk.core.fliter;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * User: Pratham
 * Date: 18/04/13  Time: 01:29
*/
public class OrderSplitterFilter {

    public static Map<String, List<CartLineItem>> classifyOrder(Order order) {
        Map<String, List<CartLineItem>> bucketCartLineItems = new HashMap<String, List<CartLineItem>>();
        for (CartLineItem cartLineItem : order.getCartLineItems()) {
            if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                Product product = cartLineItem.getProductVariant().getProduct();
                if (product.isService()) {
                    bucketCartLineItems = getPutMap(bucketCartLineItems, "Service", cartLineItem);
                    continue;
                }
                if (product.isDropShipping()) {
                    String tinNumber = product.getSupplier().getTinNumber();
                    bucketCartLineItems = getPutMap(bucketCartLineItems, tinNumber, cartLineItem);
                    continue;
                }
                if (product.isGroundShipping()) {
                    bucketCartLineItems = getPutMap(bucketCartLineItems, "Ground", cartLineItem);
                    continue;
                }
                bucketCartLineItems = getPutMap(bucketCartLineItems, "Product", cartLineItem);
            }
        }
        return bucketCartLineItems;
    }

    public static  Map<String, List<CartLineItem>> bucketCartLineItems(List<CartLineItem> cartLineItems){
        Map<String, List<CartLineItem>> bucketCartLineItems = new HashMap<String, List<CartLineItem>>();
        for (CartLineItem cartLineItem : cartLineItems) {
            Product product = cartLineItem.getProductVariant().getProduct();
            Long maxDays = product.isJit() ? product.getMaxDays() : 3; //if an inStock product is ordered, all such products lies in same bucket
            if(maxDays == null || maxDays <= 3){
                bucketCartLineItems = getPutMap(bucketCartLineItems, "B3", cartLineItem);
            }else if(maxDays <= 6){
                bucketCartLineItems = getPutMap(bucketCartLineItems, "B6", cartLineItem);
            }else if(maxDays <= 9){
                bucketCartLineItems = getPutMap(bucketCartLineItems, "B9", cartLineItem);
            }else if(maxDays <= 12){
                bucketCartLineItems = getPutMap(bucketCartLineItems, "B12", cartLineItem);
            }else {
                bucketCartLineItems = getPutMap(bucketCartLineItems, "BMX", cartLineItem);
            }
        }
        return bucketCartLineItems;
    }

    private static Map<String, List<CartLineItem>> getPutMap(Map<String, List<CartLineItem>> bucketCartLineItems, String key, CartLineItem cartLineItem) {
        List<CartLineItem> currentCLI = new ArrayList<CartLineItem>();
        currentCLI.add(cartLineItem);
        if (bucketCartLineItems.containsKey(key)) {
            List<CartLineItem> keyValue = bucketCartLineItems.get(key);
            keyValue.addAll(currentCLI);
            bucketCartLineItems.put(key, keyValue);
        } else {
            bucketCartLineItems.put(key, currentCLI);
        }
        return bucketCartLineItems;
    }

}
