package com.hk.api.edge.integration.response.cart;

import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

/**
 * @author Rimal
 */
@SuppressWarnings("serial")
public class CartSummaryFromHKR extends AbstractResponseFromHKR {

    private int itemsInCart;

    public int getItemsInCart() {
        return itemsInCart;
    }

    public void setItemsInCart(int itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    @Override
    protected String[] getKeys() {
        return new String[] { "noItemsInCart", "exception", "msgs" };
    }

    @Override
    protected Object[] getValues() {
        return new Object[] { this.itemsInCart, this.exception, this.msgs };
    }
}
