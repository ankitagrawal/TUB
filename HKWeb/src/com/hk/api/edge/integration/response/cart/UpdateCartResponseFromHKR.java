package com.hk.api.edge.integration.response.cart;

import com.hk.api.edge.integration.response.AbstractResponseFromHKR;
import com.hk.edge.constants.DtoJsonConstants;

@SuppressWarnings("serial")
public class UpdateCartResponseFromHKR extends AbstractResponseFromHKR {

    private String             lastAddedItemName;
    private CartSummaryFromHKR cartSummaryFromHKR;
    private Long               userId;
    private String  loginForUser;

    public UpdateCartResponseFromHKR(Long userId) {
        this.userId = userId;
    }

    public CartSummaryFromHKR getCartSummaryFromHKR() {
        return cartSummaryFromHKR;
    }

    public void setCartSummaryFromHKR(CartSummaryFromHKR cartSummaryFromHKR) {
        this.cartSummaryFromHKR = cartSummaryFromHKR;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLastAddedItemName() {
        return lastAddedItemName;
    }

    public void setLastAddedItemName(String lastAddedItemName) {
        this.lastAddedItemName = lastAddedItemName;
    }
    
    

    public String getLoginForUser() {
        return loginForUser;
    }

    public void setLoginForUser(String loginForUser) {
        this.loginForUser = loginForUser;
    }

    @Override
    protected String[] getKeys() {
        return new String[] { DtoJsonConstants.CART_SUMMARY, DtoJsonConstants.NAME, DtoJsonConstants.LOGIN };

    }

    @Override
    protected Object[] getValues() {
        return new Object[] { this.cartSummaryFromHKR, this.lastAddedItemName, this.loginForUser };
    }

}