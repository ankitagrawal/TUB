package com.hk.web.action.core.subscription;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.fliter.SubscriptionFilter;
import com.hk.domain.builder.CartLineItemBuilder;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.user.Address;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/4/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionUpdateAction extends BaseAction implements ValidationErrorHandler {
  Subscription subscription;
  PricingSubDto                     pricingSubDto;
  SubscriptionProduct subscriptionProduct;

  @Autowired
  SubscriptionService subscriptionService;
  @Autowired
  OrderManager orderManager;
  @Autowired
  PricingEngine pricingEngine;
  @Autowired
  CartFreebieService cartFreebieService;
  @Autowired
  SubscriptionProductService subscriptionProductService;

  @DontValidate
  @DefaultHandler
  public Resolution editSubscription() {

    subscriptionProduct = subscriptionProductService.findByProduct(subscription.getProductVariant().getProduct());

    HealthkartResponse healthkartResponse;

    healthkartResponse= new HealthkartResponse(HealthkartResponse.STATUS_OK, "");

    return new ForwardResolution("/pages/modal/editSubscription.jsp");
  }

  @HandlesEvent("save")
  @JsonHandler
  public Resolution saveSubscription(){
    subscriptionService.save(subscription);

    HealthkartResponse healthkartResponse;

    healthkartResponse= new HealthkartResponse(HealthkartResponse.STATUS_OK, "");
    return new JsonResolution(healthkartResponse);
  }

  @HandlesEvent("abandon")
  @JsonHandler
  public Resolution abandonSubscription(){
    subscriptionService.abandonSubscription(subscription);
    HealthkartResponse healthkartResponse;
    // there is a null pointer here (prodbably getPricipalUser() --> putting null check
    if (getPrincipalUser() != null) {
      Order order = orderManager.getOrCreateOrder(getPrincipalUser());
      Address address = order.getAddress() != null ? order.getAddress() : new Address();
      Set<Subscription> inCartSubscriptions= new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter();

      PricingDto pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D, inCartSubscriptions), address);

       CartLineItem cartLineItem=new CartLineItemBuilder().forSubscription(subscription).build();
      pricingSubDto = new PricingSubDto(pricingDto, cartLineItem);

      String freebieBanner = cartFreebieService.getFreebieBanner(order);
      healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, freebieBanner, pricingSubDto);


      return new JsonResolution(healthkartResponse);
    } else {
       healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "fail", pricingSubDto);

      return new JsonResolution(healthkartResponse);
    }
    /*healthkartResponse= new HealthkartResponse(HealthkartResponse.STATUS_OK, "");
    return new JsonResolution(healthkartResponse);*/
  }

  public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
    return new JsonResolution(validationErrors, getContext().getLocale());
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setSubscription(Subscription subscription) {
    this.subscription = subscription;
  }

  public PricingSubDto getPricingSubDto() {
    return pricingSubDto;
  }

  public void setPricingSubDto(PricingSubDto pricingSubDto) {
    this.pricingSubDto = pricingSubDto;
  }

  public OrderManager getOrderManager() {
    return orderManager;
  }

  public void setOrderManager(OrderManager orderManager) {
    this.orderManager = orderManager;
  }

  public PricingEngine getPricingEngine() {
    return pricingEngine;
  }

  public void setPricingEngine(PricingEngine pricingEngine) {
    this.pricingEngine = pricingEngine;
  }

  public CartFreebieService getCartFreebieService() {
    return cartFreebieService;
  }

  public void setCartFreebieService(CartFreebieService cartFreebieService) {
    this.cartFreebieService = cartFreebieService;
  }

  public SubscriptionService getSubscriptionService() {
    return subscriptionService;
  }

  public void setSubscriptionService(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  public SubscriptionProduct getSubscriptionProduct() {
    return subscriptionProduct;
  }

  public void setSubscriptionProduct(SubscriptionProduct subscriptionProduct) {
    this.subscriptionProduct = subscriptionProduct;
  }

  public SubscriptionProductService getSubscriptionProductService() {
    return subscriptionProductService;
  }

  public void setSubscriptionProductService(SubscriptionProductService subscriptionProductService) {
    this.subscriptionProductService = subscriptionProductService;
  }
}
