package com.hk.api.edge.integration.resource.cart;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.edge.integration.pact.service.cart.HybridCartService;
import com.hk.api.edge.integration.request.variant.AddProductVariantToCartRequest;
import com.hk.api.edge.integration.request.variant.AddVariantWithExtraOptions;
import com.hk.api.edge.integration.response.cart.CartSummaryFromHKR;
import com.hk.api.edge.integration.response.cart.UpdateCartResponseFromHKR;
import com.hk.constants.edge.MessageConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItemExtraOption;
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
import com.hk.report.dto.order.ProductLineItemWithExtraOptionsDto;
import com.hk.util.json.JSONResponseBuilder;

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
    public String getUserCartSummaryFromHKR(@PathParam("usrId") Long userId) {
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

  @SuppressWarnings("deprecation")
  @POST
  @Path("/extOpt/add")
  public String addProductVariantToHKRCartWithExtOpt(AddVariantWithExtraOptions addVariantWithExtraOptions){
    if (StringUtils.isNotBlank(addVariantWithExtraOptions.getOldVariantId())) {
      User user = null;
      if (addVariantWithExtraOptions.getUserId() != null) {
        user = getUserService().getUserById(addVariantWithExtraOptions.getUserId());
      }
      if (user == null) {
        user = getUserManager().createAndLoginAsGuestUser(null, null);
      }

      Order order = getOrderManager().getOrCreateOrder(user);
      UpdateCartResponseFromHKR updateCartResponseFromHKR = new UpdateCartResponseFromHKR(user.getId());
      ProductVariant productVariant = getProductVariantService().getVariantById(addVariantWithExtraOptions.getOldVariantId());
      if(productVariant!=null){
        productVariant.setQty(1L);
        try {
          List<ProductLineItemWithExtraOptionsDto> productLineItemWithExtraOptionsDtos = new ArrayList<ProductLineItemWithExtraOptionsDto>();
          if(addVariantWithExtraOptions.getLeftExtOpt()!=null && StringUtils.isNotBlank(addVariantWithExtraOptions.getLeftExtOpt())){
            productLineItemWithExtraOptionsDtos.add(getProductLineItemWithExtraOptionsDtoByExtOptions(productVariant, addVariantWithExtraOptions.getLeftExtOpt()));
          }
          if(addVariantWithExtraOptions.getRightExtOpt()!=null && StringUtils.isNotBlank(addVariantWithExtraOptions.getRightExtOpt())){
            productLineItemWithExtraOptionsDtos.add(getProductLineItemWithExtraOptionsDtoByExtOptions(productVariant, addVariantWithExtraOptions.getRightExtOpt()));
          }
          for (ProductLineItemWithExtraOptionsDto dto : productLineItemWithExtraOptionsDtos) {
            List<CartLineItemExtraOption> extraOptions = dto.getExtraOptions();
            getOrderManager().createLineItems(productVariant, extraOptions, order, null);
            userProductHistoryDao.updateIsAddedToCart(productVariant.getProduct(), user, order);
          }
          Long itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size()) + 1L;
          CartSummaryFromHKR cartSummaryFromHKR = new CartSummaryFromHKR();
          cartSummaryFromHKR.setItemsInCart(Integer.valueOf(itemsInCart.toString()));
          updateCartResponseFromHKR.setCartSummaryFromHKR(cartSummaryFromHKR);
          updateCartResponseFromHKR.setLastAddedItemName(productVariant.getVariantName());
          updateCartResponseFromHKR.setLoginForUser(user.getLogin());
          return new JSONResponseBuilder().addField("results",updateCartResponseFromHKR).build();
        } catch (OutOfStockException e) {
          updateCartResponseFromHKR.setException(true).addMessage(MessageConstants.PRODUCT_OOS);
          return new JSONResponseBuilder().addField("results",updateCartResponseFromHKR).build();
        } catch(Exception ex){
          updateCartResponseFromHKR.setException(true).addMessage(MessageConstants.UNABLE_TO_ADD_TO_CART);
          return new JSONResponseBuilder().addField("results",updateCartResponseFromHKR).build();
        }
      }else{
        updateCartResponseFromHKR.setException(true).addMessage(MessageConstants.UNABLE_TO_ADD_TO_CART);
      }
    }
    return new JSONResponseBuilder().addField("results", MessageConstants.UNABLE_TO_ADD_TO_CART).build();
  }

  private ProductLineItemWithExtraOptionsDto getProductLineItemWithExtraOptionsDtoByExtOptions(ProductVariant productVariant, String extOption){
    ProductLineItemWithExtraOptionsDto productLineItemWithExtraOptionsDto = new ProductLineItemWithExtraOptionsDto();
    productLineItemWithExtraOptionsDto.setProductVariant(productVariant);
    String[] extOpt = extOption.split(",");
    List<CartLineItemExtraOption> cartLineItemExtraOptions = new ArrayList<CartLineItemExtraOption>();
    for(String exOp : extOpt){
      CartLineItemExtraOption cartLineItemExtraOption = new CartLineItemExtraOption();
      cartLineItemExtraOption.setName(exOp.split(":")[0]);
      cartLineItemExtraOption.setValue(exOp.split(":")[1]);
      cartLineItemExtraOptions.add(cartLineItemExtraOption);
    }
    productLineItemWithExtraOptionsDto.setExtraOptions(cartLineItemExtraOptions);
    return productLineItemWithExtraOptionsDto;
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