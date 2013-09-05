package com.hk.api.edge.integration.resource.cart;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.edge.integration.pact.service.cart.HybridCartService;
import com.hk.api.edge.integration.request.variant.AddProductVariantToCartRequest;
import com.hk.api.edge.integration.response.cart.CartSummaryFromHKR;
import com.hk.api.edge.integration.response.cart.UpdateCartResponseFromHKR;
import com.hk.util.json.JSONResponseBuilder;

/**
 * @author Rimal
 */
@Component
@Path("/cart/")
public class HybridCartResource {

    @Autowired
    private HybridCartService hybridCartService;

    @GET
    @Path("/summary")
    @Produces("application/json")
    public String getUserCartSummaryFromHKR() {
        CartSummaryFromHKR cartSummaryFromHKR = getHybridCartService().getUserCartSummaryFromHKR();
        return new JSONResponseBuilder().addField("results", cartSummaryFromHKR).build();
    }
    
    @POST
    @Path("/add")
    public String addProductVariantToHKRCart(AddProductVariantToCartRequest addProductVariantToCartRequest) {
        UpdateCartResponseFromHKR updateCartResponseFromHKR = new UpdateCartResponseFromHKR(addVariantToCartRequest.getStoreId(), addVariantToCartRequest.getUserId());

      try {
        updateShoppingCartResponse = getCartService().addVariantToCart(addVariantToCartRequest);
      } catch (VariantOOSException e) {
        updateShoppingCartResponse.setException(true).addMessage(MessageConstants.PRODUCT_OOS);
      } catch (InsufficientStockForVariantException e) {
        updateShoppingCartResponse.setException(true).addMessage(MessageConstants.REQ_QTY_NA);
      } catch (InvalidCartQtyForVariantException e) {
        updateShoppingCartResponse.setException(true).addMessage(MessageConstants.INVALID_CART_QTY);
      }

      if (null != updateShoppingCartResponse) {
        updateShoppingCartResponse.addMessage(MessageConstants.PRODUCT_ADDED_TO_CART);
      } else {
        updateShoppingCartResponse = new UpdateShoppingCartResponse(addVariantToCartRequest.getStoreId(), addVariantToCartRequest.getUserId());
        updateShoppingCartResponse.addMessage(MessageConstants.UNABLE_TO_ADD_TO_CART);
      }

      return updateShoppingCartResponse;
    }

   

    public HybridCartService getHybridCartService() {
        return hybridCartService;
    }

}