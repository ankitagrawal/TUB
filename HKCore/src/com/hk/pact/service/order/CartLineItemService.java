package com.hk.pact.service.order;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.CartLineItemType;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;

import java.util.Collection;
import java.util.Set;

public interface CartLineItemService {

    public CartLineItem save(CartLineItem cartLineItem);

    public void remove(Long id);

    public CartLineItemType getCartLineItemType(EnumCartLineItemType enumCartLineItemType);

    /**
     * create a cart line item with basic attributes filled to be added to cart.
     * 
     * @param productVariant
     * @param order
     * @return
     */
    public CartLineItem createCartLineItemWithBasicDetails(ProductVariant productVariant, Order order);

    public CartLineItem getMatchingCartLineItemFromOrder(Order order, CartLineItemMatcher cartLineItemMatcher);
}
