package com.hk.web.action.core.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.dao.catalog.combo.ComboInstanceDao;
import com.hk.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.service.order.CartLineItemService;
import com.hk.web.HealthkartResponse;

@Component
public class CartLineItemUpdateAction extends BaseAction {

    @SuppressWarnings("unused")
    private Logger                    logger                      = LoggerFactory.getLogger(CartLineItemUpdateAction.class);

    CartLineItem                      cartLineItem;
    Map<CartLineItem, Long>           cartLineItemsInnitialQtyMap = new HashMap<CartLineItem, Long>();
    ComboInstance                     comboInstance;
    PricingSubDto                     pricingSubDto;
    @Autowired
    OrderManager                      orderManager;
    @Autowired
    PricingEngine                     pricingEngine;
    @Autowired
    ComboInstanceDao                  comboInstanceDao;
    @Autowired
    ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao;
    @Autowired
    CartLineItemService               cartLineItemService;

    @JsonHandler
    public Resolution pre() {

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
                cartLineItem = cartLineItemService.save(cartLineItem);
            }
            orderManager.trimEmptyLineItems(cartLineItem.getOrder());
        }

        noCache();

        // there is a null pointer here (prodbably getPricipalUser() --> putting null check
        if (getPrincipalUser() != null) {
            Order order = orderManager.getOrCreateOrder(getPrincipalUser());
            Address address = order.getAddress() != null ? order.getAddress() : new Address();
            PricingDto pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);

            if (cartLineItem != null && cartLineItem.getComboInstance() != null && cartLineItem.getComboInstance().getId() != null) {
                List<CartLineItem> brotherLineItems = comboInstanceDao.getSiblingLineItems(cartLineItem);
                for (CartLineItem li : brotherLineItems) {
                    pricingSubDto = new PricingSubDto(pricingDto, li);
                }
            } else {
                pricingSubDto = new PricingSubDto(pricingDto, cartLineItem);
            }
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", pricingSubDto);

            return new JsonResolution(healthkartResponse);
        } else {
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "fail", pricingSubDto);

            return new JsonResolution(healthkartResponse);
        }
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
