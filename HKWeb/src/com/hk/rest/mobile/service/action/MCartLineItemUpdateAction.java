package com.hk.rest.mobile.service.action;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.user.Address;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.manager.OrderManager;
import com.hk.pricing.PricingEngine;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.util.HKImageUtils;
import com.hk.web.HealthkartResponse;
import com.hk.dto.pricing.PricingDto;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 7, 2012
 * Time: 2:01:12 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.*;

import net.sourceforge.stripes.action.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.gson.JsonUtils;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.web.HealthkartResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.rest.mobile.service.model.MCartLineItemsJSONResponse;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.order.EnumCartLineItemType;
import com.shiro.PrincipalImpl;

import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletResponse;

@Path("/mRemoveItem")
@Component
public class MCartLineItemUpdateAction extends MBaseAction {

    @SuppressWarnings("unused")
    private Logger logger                      = LoggerFactory.getLogger(MCartLineItemUpdateAction.class);

    CartLineItem cartLineItem;
    Map<CartLineItem, Long> cartLineItemsInnitialQtyMap = new HashMap<CartLineItem, Long>();
    ComboInstance comboInstance;
    PricingSubDto pricingSubDto;
    @Autowired
    OrderManager orderManager;
    @Autowired
    PricingEngine pricingEngine;
    @Autowired
    ComboInstanceDao comboInstanceDao;
    @Autowired
    ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao;
    @Autowired
    CartLineItemService cartLineItemService;
    @Autowired
    CartFreebieService cartFreebieService;
    @Autowired
    CartLineItemDao cartLineItemDao;
    @Autowired
    UserManager userManager;

    private Order order;

    @JsonHandler
    @DefaultHandler
    @GET
    @Path("/removeItemfromCart/")
    @Produces("application/json")
    public String removeItemfromCart(@QueryParam("cartLineItemId")long cartLineItemId,
                                     @QueryParam("qty")long qty,
                          @Context HttpServletResponse response) {

        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        User user = null;
        if (getSecurityManager().getSubject().getPrincipal() != null) {
            user = getUserService().getUserById(((PrincipalImpl) getSecurityManager().getSubject().getPrincipal()).getId());
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }
        if (user != null) {
            order = orderManager.getOrCreateOrder(user);
        }

        for(CartLineItem item:order.getCartLineItems()){
            if(cartLineItemId==item.getId().longValue()){
                cartLineItem = item;
                cartLineItem.setQty(qty);
                break;
            }
        }

        if (cartLineItem != null && cartLineItem.getHkPrice() != null && cartLineItem.getHkPrice() != 0D) {
            if (cartLineItem.getComboInstance() != null) {
                List<CartLineItem> siblingLineItems = comboInstanceDao.getSiblingLineItems(cartLineItem);
                for (CartLineItem cartLi : siblingLineItems) {  
                    cartLi.setQty(cartLi.getComboInstance().getComboInstanceProductVariant(cartLi.getProductVariant()).getQty() * comboInstance.getQty());
                    cartLi = cartLineItemService.save(cartLi);
                }
            } else {
                if (cartLineItem.getQty() == null) {
                    cartLineItem.setQty(1L);
                }
                if (cartLineItem.getDiscountOnHkPrice() == null) {
                    cartLineItem.setDiscountOnHkPrice(0D);
                }
                if (orderManager.isStepUpAllowed(cartLineItem)) {
                  cartLineItem = cartLineItemService.save(cartLineItem);

                }else{
                  cartLineItemDao.refresh(cartLineItem);
                  healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "fail", cartLineItem.getQty());
                  return JsonUtils.getGsonDefault().toJson(healthkartResponse);
                }
            }
            order = orderManager.trimEmptyLineItems(cartLineItem.getOrder());
        }


        Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        List<MCartLineItemsJSONResponse> cartItemsList = new ArrayList<MCartLineItemsJSONResponse>();
        MCartLineItemsJSONResponse cartItemResponse;
        for (CartLineItem lineItem : cartLineItems) {
            if (lineItem != null && lineItem.getProductVariant() != null) {
                ProductVariant productVariant = lineItem.getProductVariant();
                cartItemResponse = new MCartLineItemsJSONResponse();
                cartItemResponse.setDiscountOnHkPrice(lineItem.getDiscountOnHkPrice());
                cartItemResponse.setHkPrice(lineItem.getHkPrice());
                cartItemResponse.setId(lineItem.getId());
                cartItemResponse.setName(productVariant.getProduct().getName());
                if(null!=productVariant.getProduct() && null!=productVariant.getProduct().getMainImageId())
                	cartItemResponse.setImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize,productVariant.getProduct().getMainImageId(),false));
                else
                	cartItemResponse.setImageUrl(getImageUrl()+productVariant.getProduct().getId()+MHKConstants.IMAGETYPE);
                if(null!=lineItem.getLineItemType())
                cartItemResponse.setLineItemType(lineItem.getLineItemType().getName());
                cartItemResponse.setMarkedPrice(lineItem.getMarkedPrice());
                cartItemResponse.setOrder(lineItem.getOrder().getId());
                cartItemResponse.setQty(lineItem.getQty());
                if(lineItem.getQty()<=0){
                	continue;
                }
                cartItemResponse.setCartLineItemId(lineItem.getId().toString());
                cartItemsList.add(cartItemResponse);
            }
        }
        order = orderManager.trimEmptyLineItems(order);

        healthkartResponse = new HealthkartResponse(status, message, cartItemsList);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);
        noCache(response);

        return jsonBuilder;

    }

    public CartLineItem getCartLineItem() {
        return cartLineItem;
    }

    public void setcartLineItem(CartLineItem cartLineItem) {
        this.cartLineItem = cartLineItem;
    }

    public ComboInstance getComboInstance() {
        return comboInstance;
    }

    public void setComboInstance(ComboInstance comboInstance) {
        this.comboInstance = comboInstance;
    }
}
