package com.hk.api.edge.ext.response.cart;

import com.hk.api.edge.ext.response.AbstractApiBaseResponse;

/**
 * @author Rimal
 */
public class CartSummaryApiResponse extends AbstractApiBaseResponse {

    private int itemsInCart;


    public int getItemsInCart() {
        return itemsInCart;
    }

    public void setItemsInCart(int itemsInCart) {
        this.itemsInCart = itemsInCart;
    }


    @Override
    protected String[] getKeys() {
        return new String[]{
                "noItemsInCart"
        };
    }

    @Override
    protected Object[] getValues() {
        return new Object[]{
                this.itemsInCart
        };
    }
}
