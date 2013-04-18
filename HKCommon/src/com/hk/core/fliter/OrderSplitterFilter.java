package com.hk.core.fliter;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
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
            List<CartLineItem> currentCLI = new ArrayList<CartLineItem>();
            currentCLI.add(cartLineItem);
            if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                Product product = cartLineItem.getProductVariant().getProduct();
                ProductVariant productVariant = cartLineItem.getProductVariant();
                if (product.isService()) {
                    bucketCartLineItems.put("Service " + productVariant.getId(), currentCLI);
                    continue;
                }
                if (product.isDropShipping()) {
                    String tinNumber = product.getSupplier().getTinNumber();
                    if (bucketCartLineItems.containsKey(tinNumber)) {
                        List<CartLineItem> keyValue = bucketCartLineItems.get(tinNumber);
                        keyValue.addAll(currentCLI);
                        bucketCartLineItems.put(tinNumber, keyValue);
                        continue;
                    } else {
                        bucketCartLineItems.put(tinNumber, currentCLI);
                        continue;
                    }
                }
                if (product.isGroundShipping()) {
                    if (bucketCartLineItems.containsKey("Ground")) {
                        List<CartLineItem> keyValue = bucketCartLineItems.get("Ground");
                        keyValue.addAll(currentCLI);
                        bucketCartLineItems.put("Ground", keyValue);
                        continue;
                    } else {
                        bucketCartLineItems.put("Ground", currentCLI);
                        continue;
                    }
                }
                if (bucketCartLineItems.containsKey("Product")) {
                    List<CartLineItem> keyValue = bucketCartLineItems.get("Product");
                    keyValue.addAll(currentCLI);
                    bucketCartLineItems.put("Product", keyValue);
                } else {
                    bucketCartLineItems.put("Product", currentCLI);
                }
            }
        }
        return bucketCartLineItems;
    }

}
