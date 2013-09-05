package com.hk.api.edge.integration.resource.cart;

import com.hk.api.edge.constants.MessageConstants;
import com.hk.api.edge.integration.pact.service.cart.HybridCartService;
import com.hk.api.edge.integration.request.variant.AddProductVariantToCartRequest;
import com.hk.api.edge.integration.response.cart.CartSummaryFromHKR;
import com.hk.api.edge.integration.response.cart.UpdateCartResponseFromHKR;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.edge.pact.service.HybridStoreVariantService;
import com.hk.edge.response.variant.StoreVariantBasicResponse;
import com.hk.exception.OutOfStockException;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.util.json.JSONResponseBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rimal
 */
@Component
@Path("/cart/")
public class HybridCartResource {

    @Autowired
    private HybridCartService hybridCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    UserCartDao userCartDao;
    @Autowired
    UserProductHistoryDao userProductHistoryDao;
    @Autowired
    private HybridStoreVariantService hybridStoreVariantService;

    @GET
    @Path("{usrId}/summary/")
    @Produces("application/json")
    public String getUserCartSummaryFromHKR(@QueryParam("usrId") Long userId) {
        CartSummaryFromHKR cartSummaryFromHKR = getHybridCartService().getUserCartSummaryFromHKR(userId);
        return new JSONResponseBuilder().addField("results", cartSummaryFromHKR).build();
    }

    @SuppressWarnings("deprecation")
    @POST
    @Path("/add")
    public String addProductVariantToHKRCart(AddProductVariantToCartRequest addProductVariantToCartRequest) {

        if (StringUtils.isNotBlank(addProductVariantToCartRequest.getOldVariantId())) {
            User user = null;
            if (addProductVariantToCartRequest.getUserId() != null) {
                user = getUserService().getUserById(addProductVariantToCartRequest.getUserId());
            }
            if (user == null) {
                user = getUserManager().createAndLoginAsGuestUser(null, null);
            }

            Order order = getOrderManager().getOrCreateOrder(user);
            List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();

            List<ProductVariant> productVariantList = new ArrayList<ProductVariant>();
            ProductVariant productVariantToAdd = getProductVariantService().getVariantById(addProductVariantToCartRequest.getOldVariantId());
            productVariantToAdd.setQty(1L);
            productVariantList.add(productVariantToAdd);

            UpdateCartResponseFromHKR updateCartResponseFromHKR = new UpdateCartResponseFromHKR(user.getId());

            try {
                if (productVariantList != null && productVariantList.size() > 0) {
                    for (ProductVariant productVariant : productVariantList) {
                        if (productVariant != null && productVariant.getQty() != null
                                && productVariant.getQty() > 0) {
                            selectedProductVariants.add(productVariant);
                            userCartDao.addToCartHistory(productVariant.getProduct(), user);
                            userProductHistoryDao.updateIsAddedToCart(productVariant.getProduct(), user, order);
                        }
                    }
                }

                orderManager.createLineItems(selectedProductVariants, order, null, null, null);
            } catch (OutOfStockException e) {
                updateCartResponseFromHKR.setException(true).addMessage(MessageConstants.PRODUCT_OOS);
            }

            Long itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size()) + 1L;

            StoreVariantBasicResponse storeVariantBasicApiResponse = getHybridStoreVariantService().getStoreVariantBasicDetailsFromEdge(
                    addProductVariantToCartRequest.getOldVariantId());

            if (null != updateCartResponseFromHKR) {
                if (storeVariantBasicApiResponse != null) {
                    updateCartResponseFromHKR.setLastAddedItemName(storeVariantBasicApiResponse.getName());
                } else {
                    updateCartResponseFromHKR.setLastAddedItemName(productVariantToAdd.getProduct().getName());
                }
                CartSummaryFromHKR cartSummaryFromHKR = new CartSummaryFromHKR();
                if (itemsInCart != null) {
                    cartSummaryFromHKR.setItemsInCart(Integer.valueOf(itemsInCart.toString()));
                }
                updateCartResponseFromHKR.setCartSummaryFromHKR(cartSummaryFromHKR);
                updateCartResponseFromHKR.setLoginForUser(user.getLogin());
                updateCartResponseFromHKR.addMessage(MessageConstants.PRODUCT_ADDED_TO_CART);
            } else {
                updateCartResponseFromHKR = new UpdateCartResponseFromHKR(user.getId());
                updateCartResponseFromHKR.addMessage(MessageConstants.UNABLE_TO_ADD_TO_CART);
            }

            return new JSONResponseBuilder().addField("results", updateCartResponseFromHKR).build();

        }

        return new JSONResponseBuilder().addField("results", MessageConstants.UNABLE_TO_ADD_TO_CART).build();

    }

    public HybridCartService getHybridCartService() {
        return hybridCartService;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public HybridStoreVariantService getHybridStoreVariantService() {
        return hybridStoreVariantService;
    }

}