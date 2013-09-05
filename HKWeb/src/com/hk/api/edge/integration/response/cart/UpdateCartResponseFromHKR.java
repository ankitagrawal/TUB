package com.hk.api.edge.integration.response.cart;

import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

@SuppressWarnings("serial")
public class UpdateCartResponseFromHKR extends AbstractResponseFromHKR {

    @Override
    protected String[] getKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Object[] getValues() {
        // TODO Auto-generated method stub
        return null;
    }

   /* private String lastAddedItemName;

    private CartSummaryResponse cartSummaryResponse;
    private Long userId;


    public UpdateShoppingCartResponseFromHKR(Long storeId, Long userId) {
      super(storeId);
      this.userId = userId;
    }

    public CartSummaryResponse getCartSummaryResponse() {
      return cartSummaryResponse;
    }

    public void setCartSummaryResponse(CartSummaryResponse cartSummaryResponse) {
      this.cartSummaryResponse = cartSummaryResponse;
    }

    public Long getUserId() {
      return userId;
    }

    public void setUserId(Long userId) {
      this.userId = userId;
    }

    private ShoppingCartVariant lastUpdateShoppingCartVariant;

     public UpdateShoppingCartResponse(Long storeId, Long userId, Long shoppingCartId) {
       super(storeId, userId, shoppingCartId);
     }

     public ShoppingCartVariant getLastUpdateShoppingCartVariant() {
       return lastUpdateShoppingCartVariant;
     }

     public void setLastUpdateShoppingCartVariant(ShoppingCartVariant lastUpdateShoppingCartVariant) {
       this.lastUpdateShoppingCartVariant = lastUpdateShoppingCartVariant;
     }

    

    public String getLastAddedItemName() {
      return lastAddedItemName;
    }

    public void setLastAddedItemName(String lastAddedItemName) {
      this.lastAddedItemName = lastAddedItemName;
    }


    @Override
    protected List<String> getKeys() {
      List<String> keyList = super.getKeys();
      keyList.add(DtoJsonConstants.CART_SUMMARY);
      keyList.add(DtoJsonConstants.NAME);

      return keyList;
    }

    @Override
    protected List<Object> getValues() {
      List<Object> valueList = super.getValues();
      valueList.add(this.cartSummaryResponse);
      valueList.add(this.lastAddedItemName);

      return valueList;
    }
  }*/
}