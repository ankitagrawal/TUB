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
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.Address;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.web.HealthkartResponse;

@Component
public class LineItemUpdateAction extends BaseAction {

  CartLineItem cartLineItem;
  Map<LineItem, Long> lineItemsInnitialQtyMap = new HashMap<LineItem, Long>();
  ComboInstance comboInstance;
  PricingSubDto pricingSubDto;

  @Autowired
  LineItemDao lineItemDao;
  @Autowired
  CartLineItemDao cartLineItemDao;
  @Autowired
  OrderManager orderManager;
  @Autowired
  PricingEngine pricingEngine;
  @Autowired
  ComboInstanceDao comboInstanceDao;
  @Autowired
  ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao;

  Logger logger = LoggerFactory.getLogger(LineItemUpdateAction.class);

  @JsonHandler
  public Resolution pre() {

    if (cartLineItem != null && cartLineItem.getHkPrice() != null) {
      if (cartLineItem.getComboInstance() != null) {
        for (CartLineItem li : comboInstanceDao.getSiblingLineItems(cartLineItem)) {
          li.setQty(li.getComboInstance().getComboInstanceProductVariant(li.getProductVariant()).getQty() * comboInstance.getQty());
          li = cartLineItemDao.save(li);
        }
      } else {
        if (cartLineItem.getQty() == null) {
          cartLineItem.setQty(1L);
        }
        if (cartLineItem.getDiscountOnHkPrice() == null) {
          cartLineItem.setDiscountOnHkPrice(0D);
        }
        try {
          cartLineItem = cartLineItemDao.save(cartLineItem);
        } catch (Exception e) {
          logger.info("Exception for LineItem Id" + cartLineItem.toString());
          logger.info("Exception for LineItem Id" + cartLineItem.getId());
//          logger.info("LineItem Details are as Follows" + cartLineItem.getLineItemDetails());
        }
      }
      orderManager.trimEmptyLineItems(cartLineItem.getOrder());
    }

    noCache();

    //  there is a null pointer here (prodbably getPricipalUser() --> putting null check
    if (getPrincipalUser() != null) {
      Order order = orderManager.getOrCreateOrder(getPrincipalUser());
      Address address = order.getAddress() != null ? order.getAddress() : new Address();
      PricingDto pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);

      if (cartLineItem.getComboInstance() != null && cartLineItem.getComboInstance().getId() != null) {
        List<CartLineItem> siblingCartLineItems = comboInstanceDao.getSiblingLineItems(cartLineItem);
        if (siblingCartLineItems != null) {
          for (CartLineItem li : siblingCartLineItems) {
            pricingSubDto = new PricingSubDto(pricingDto, li);
          }
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

  public void setCartLineItem(CartLineItem cartLineItem) {
    this.cartLineItem = cartLineItem;
  }

  public ComboInstance getComboInstance() {
    return comboInstance;
  }

  public void setComboInstance(ComboInstance comboInstance) {
    this.comboInstance = comboInstance;
  }
}
