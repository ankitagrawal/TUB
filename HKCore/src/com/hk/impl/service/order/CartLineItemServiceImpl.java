package com.hk.impl.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.CartLineItemType;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.service.order.CartLineItemService;

/**
 * @author vaibhav.adlakha
 */
@Service
public class CartLineItemServiceImpl implements CartLineItemService {

    @Autowired
    private CartLineItemDao cartLineItemDao;

    @Transactional
    public CartLineItem save(CartLineItem cartLineItem) {
        return getCartLineItemDao().save(cartLineItem);
    }

	@Override
	public void remove(Long id) {
		 getCartLineItemDao().remove(id);
	}

	@Override
    public CartLineItemType getCartLineItemType(EnumCartLineItemType enumCartLineItemType) {
        return getCartLineItemDao().get(CartLineItemType.class, enumCartLineItemType.getId());
    }

    /**
     * create a cart line item with basic attributes filled to be added to cart.
     * 
     * @param productVariant
     * @param order
     * @return
     */
    public CartLineItem createCartLineItemWithBasicDetails(ProductVariant productVariant, Order order) {
        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.setProductVariant(productVariant);
        cartLineItem.setQty(productVariant.getQty());
        cartLineItem.setOrder(order);
        cartLineItem.setMarkedPrice(productVariant.getMarkedPrice());
        cartLineItem.setHkPrice(productVariant.getHkPrice(null));
        cartLineItem.setDiscountOnHkPrice(new Double(0));
        cartLineItem.setLineItemType(getCartLineItemType(EnumCartLineItemType.Product));

        return cartLineItem;
    }

    public CartLineItem getMatchingCartLineItemFromOrder(Order order, CartLineItemMatcher cartLineItemMatcher) {

        return cartLineItemMatcher.match(order.getCartLineItems());
    }

    public CartLineItemDao getCartLineItemDao() {
        return cartLineItemDao;
    }

    public void setCartLineItemDao(CartLineItemDao cartLineItemDao) {
        this.cartLineItemDao = cartLineItemDao;
    }

}
