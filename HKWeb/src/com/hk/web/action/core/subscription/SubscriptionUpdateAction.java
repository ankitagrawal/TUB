package com.hk.web.action.core.subscription;

import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.SubscriptionConstants;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.util.UIDateTypeConverter;
import com.hk.web.HealthkartResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/4/12
 * Time: 2:17 PM
 */
public class SubscriptionUpdateAction extends BaseAction implements ValidationErrorHandler {

    @ValidateNestedProperties({
            @Validate(field="subscriptionPeriodDays",required = true,mask = "[0-9]{1,9}",on="save"),
            @Validate(field = "qty",required = true, minvalue = 1, mask = "[0-9]{1,9}",on="save"),
            @Validate(field = "frequencyDays",required = true,minvalue = 1, mask = "[0-9]{1,9}",on="save"),
            @Validate(field = "qtyPerDelivery",required = true, minvalue = 1, mask = "[0-9]{1,9}",on="save"),
            @Validate(field = "startDate",required = true, converter = UIDateTypeConverter.class,on="save"),
            @Validate(field = "productVariant",required = true,on="save")
    })
    Subscription subscription;
    PricingSubDto                     pricingSubDto;
    SubscriptionProduct subscriptionProduct;

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private PricingEngine pricingEngine;
    @Autowired
    private CartFreebieService cartFreebieService;
    @Autowired
    private SubscriptionProductService subscriptionProductService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CartLineItemService cartLineItemService;

    @DefaultHandler
    public Resolution editSubscription() {

        subscriptionProduct = subscriptionProductService.findByProduct(subscription.getProductVariant().getProduct());

        return new ForwardResolution("/pages/modal/editSubscription.jsp");
    }

    @HandlesEvent("save")
    @JsonHandler
    public Resolution saveSubscription(){

        User user = null;
        if (getPrincipal() != null) {
            user = userDao.getUserById(getPrincipal().getId());
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }

        Order order = orderManager.getOrCreateOrder(user);
        HealthkartResponse healthkartResponse;
        if(subscription.getBaseOrder().getId() == order.getId()){
            subscriptionService.save(subscription);

            CartLineItem cartLineItem=subscription.getCartLineItem();
            cartLineItem.setQty(subscription.getQty());
            cartLineItem.setDiscountOnHkPrice((subscription.getHkPriceAtSubscription()-subscription.getSubscriptionPrice())*subscription.getQty());
            cartLineItemService.save(cartLineItem);

            healthkartResponse= new HealthkartResponse(HealthkartResponse.STATUS_OK, "");
            return new JsonResolution(healthkartResponse);
        }else{
            healthkartResponse= new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "please don't try to mess up with our system");
            return new JsonResolution(healthkartResponse);
        }
    }

    @HandlesEvent("abandon")
    @JsonHandler
    public Resolution abandonSubscription(){

        HealthkartResponse healthkartResponse;
        // there is a null pointer here (prodbably getPricipalUser() --> putting null check
        if (getPrincipalUser() != null) {
            Order order = orderManager.getOrCreateOrder(getPrincipalUser());
            if(subscription.getBaseOrder().getId()!=order.getId()){
                healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "please don't try to mess up with our system");
                return new JsonResolution(healthkartResponse);
            }
            subscriptionService.abandonSubscription(subscription);
            CartLineItem cartLineItem=subscription.getCartLineItem();
            cartLineItem.setQty(0L);
            cartLineItem=cartLineItemService.save(cartLineItem);

            Address address = order.getAddress() != null ? order.getAddress() : new Address();

            PricingDto pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);

//            CartLineItem cartLineItem=new CartLineItemBuilder().forSubscription(subscription).build();
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

    @ValidationMethod(on = {"save"})
    public void validateSubscriptionDays() {
        subscriptionProduct = subscriptionProductService.findByProductVariant(subscription.getProductVariant());
        if (subscriptionProduct == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Don't mess up with our product codes"));
        }else {
            if(subscription.getFrequencyDays()<subscriptionProduct.getMinFrequencyDays()|| subscription.getFrequencyDays()>subscriptionProduct.getMaxFrequencyDays()){
                getContext().getValidationErrors().add("2", new SimpleError("Enter valid no of days for subscription frequency"));
            }
            if(subscription.getSubscriptionPeriodDays()< SubscriptionConstants.minSubscriptionDays || subscription.getSubscriptionPeriodDays()>SubscriptionConstants.maxSubscriptionDays){
                getContext().getValidationErrors().add("3",new SimpleError("Enter valid no of days for total subscription period"));
            }
            if(subscription.getQtyPerDelivery()<0 || subscription.getQtyPerDelivery()>subscriptionProduct.getMaxQtyPerDelivery()){
                getContext().getValidationErrors().add("4",new SimpleError("you can only have 0 to "+subscriptionProduct.getMaxQtyPerDelivery()+" products per delivery"));
            }
            Date currentTime= BaseUtils.getCurrentTimestamp();
            int oneDay= 24*60*60*1000;
            Long days=(subscription.getStartDate().getTime() - BaseUtils.getCurrentTimestamp().getTime())/oneDay;
            if(days<-2 || days>62){
                getContext().getValidationErrors().add("4",new SimpleError("Please enter a start date with in two months from current date"));
            }
            //to do validate startDate to be within two months from current date
        }
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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public CartLineItemService getCartLineItemService() {
        return cartLineItemService;
    }

    public void setCartLineItemService(CartLineItemService cartLineItemService) {
        this.cartLineItemService = cartLineItemService;
    }
}
