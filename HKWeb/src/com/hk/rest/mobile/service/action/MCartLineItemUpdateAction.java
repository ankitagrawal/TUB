package com.hk.rest.mobile.service.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.DefaultHandler;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.shiro.Principal;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.rest.mobile.service.model.MCartLineItemsJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.util.HKImageUtils;
import com.hk.web.HealthkartResponse;

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
    CartLineItemService cartLineItemService;
    @Autowired
    CartFreebieService cartFreebieService;
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
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            user = getUserService().getUserById(((Principal) SecurityUtils.getSubject().getPrincipal()).getId());
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
                  orderManager.getCartLineItemDao().refresh(cartLineItem);
                  return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(MHKConstants.STATUS_ERROR, MHKConstants.NO_STEP_UP, cartLineItem.getQty()));
                }
            }
            orderManager.trimEmptyLineItems(cartLineItem.getOrder());
            orderManager.getCartLineItemDao().refresh(order);
        }


        Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        List<MCartLineItemsJSONResponse> cartItemsList = new ArrayList<MCartLineItemsJSONResponse>();
        MCartLineItemsJSONResponse cartItemResponse;
        for (CartLineItem lineItem : cartLineItems) {
            if (lineItem != null && lineItem.getProductVariant() != null) {
                ProductVariant productVariant = lineItem.getProductVariant();
                cartItemResponse = new MCartLineItemsJSONResponse();
                if(null!=lineItem.getDiscountOnHkPrice())
                cartItemResponse.setDiscountOnHkPrice(priceFormat.format(lineItem.getDiscountOnHkPrice()));
                if(null!=lineItem.getHkPrice())
                	cartItemResponse.setHkPrice(priceFormat.format(lineItem.getHkPrice()));
                cartItemResponse.setId(lineItem.getId());
                cartItemResponse.setName(productVariant.getProduct().getName());
                if(null!=productVariant.getProduct() && null!=productVariant.getProduct().getMainImageId())
                	cartItemResponse.setImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize,productVariant.getProduct().getMainImageId()));
                else
                	cartItemResponse.setImageUrl(getImageUrl()+productVariant.getProduct().getId()+MHKConstants.IMAGETYPE);
                if(null!=lineItem.getLineItemType())
                cartItemResponse.setLineItemType(lineItem.getLineItemType().getName());
                if(null!=lineItem.getMarkedPrice())
                cartItemResponse.setMarkedPrice(priceFormat.format(lineItem.getMarkedPrice()));
                cartItemResponse.setOrder(lineItem.getOrder().getId());
                cartItemResponse.setQty(lineItem.getQty());
                if(lineItem.getQty()<=0){
                	continue;
                }
                cartItemResponse.setCartLineItemId(lineItem.getId().toString());
                cartItemsList.add(cartItemResponse);
            }
        }
        orderManager.trimEmptyLineItems(order);
        orderManager.getCartLineItemDao().refresh(order);
        Address address = order.getAddress() != null ? order.getAddress() : new Address();
        PricingDto pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);
        Map<String,Object> cartMap = new HashMap<String,Object>();
        cartMap.put("cartItemsList", cartItemsList);
        cartMap.put("pricingDto", pricingDto);
        healthkartResponse = new HealthkartResponse(status, message, cartMap);
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
