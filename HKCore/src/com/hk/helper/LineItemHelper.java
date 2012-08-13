package com.hk.helper;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;

/**
 * @author vaibhav.adlakha
 */
public class LineItemHelper {

    
    //TODO: may be move it to line item service
    public static LineItem createLineItemWithBasicDetails(Sku sku, ShippingOrder shippingOrder, CartLineItem cartLineItem) {
        LineItem shippingOrderLineItem = new LineItem();
        shippingOrderLineItem.setSku(sku);
        shippingOrderLineItem.setShippingOrder(shippingOrder);
        shippingOrderLineItem.setCartLineItem(cartLineItem);
        shippingOrderLineItem.setQty(cartLineItem.getQty());
        
        CartLineItemConfig config = cartLineItem.getCartLineItemConfig();
        double costPrice = cartLineItem.getProductVariant().getCostPrice();
        if(config !=null){
            costPrice += config.getCostPrice();
        }
        shippingOrderLineItem.setCostPrice(costPrice);
        shippingOrderLineItem.setMarkedPrice(cartLineItem.getMarkedPrice());
        shippingOrderLineItem.setHkPrice(cartLineItem.getHkPrice());
        shippingOrderLineItem.setDiscountOnHkPrice(cartLineItem.getDiscountOnHkPrice());
        shippingOrderLineItem.setTax(sku.getTax());
        shippingOrder.getLineItems().add(shippingOrderLineItem);

        return shippingOrderLineItem;
    }
}
