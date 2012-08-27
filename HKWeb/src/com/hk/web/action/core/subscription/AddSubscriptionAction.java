package com.hk.web.action.core.subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
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
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.constants.subscription.SubscriptionConstants;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.SubscriptionFilter;
import com.hk.domain.builder.CartLineItemBuilder;
import com.hk.domain.builder.SubscriptionBuilder;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.matcher.SubscriptionMatcher;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.util.UIDateTypeConverter;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.user.SignupAction;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:00:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddSubscriptionAction extends BaseAction implements ValidationErrorHandler {
    //private static Logger logger = Logger.getLogger(AddToCartAction.class);
    @ValidateNestedProperties({
            @Validate(field="subscriptionPeriodDays",required = true,mask = "[0-9]{1,9}"),
            @Validate(field = "qty",required = true, minvalue = 1, mask = "[0-9]{1,9}"),
            @Validate(field = "frequencyDays",required = true,minvalue = 1, mask = "[0-9]{1,9}"),
            @Validate(field = "qtyPerDelivery",required = true, minvalue = 1, mask = "[0-9]{1,9}"),
            @Validate(field = "startDate",required = true, converter = UIDateTypeConverter.class),
            @Validate(field = "productVariant",required = true)
    })
    private Subscription subscription;
    private SubscriptionProduct subscriptionProduct;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserManager userManager;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserCartDao userCartDao;
    @Autowired
    private SignupAction signupAction;

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionProductService subscriptionProductService;
    @Autowired
    private SubscriptionLoggingService subscriptionLoggingService;
    @Autowired
    private CartLineItemService cartLineItemService;


    @SuppressWarnings({"unchecked", "deprecation"})
    @DefaultHandler
    @JsonHandler
    public Resolution addSubscription() {
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
        List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();
        Subscription priorSubscription=null;
        try {
            if (subscription != null &&subscription.getId()==null && subscription.getQty()>0 ) {

                priorSubscription= new SubscriptionMatcher().addProduct(subscription.getProductVariant()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).match(new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter());
                if(priorSubscription==null) {
                    SubscriptionProduct subscriptionProduct=subscriptionProductService.findByProductVariant(subscription.getProductVariant());
                    SubscriptionBuilder subscriptionBuilder=new SubscriptionBuilder();
                    subscriptionBuilder.forUser(user).forOrder(order).forSubscriptionProduct(subscriptionProduct);
                    subscriptionBuilder.forProductVariant(subscription.getProductVariant()).forQuantity(subscription.getQty(),subscription.getQtyPerDelivery()).frequency(subscription.getFrequencyDays()).startDate(subscription.getStartDate()).subscriptionPeriod(subscription.getSubscriptionPeriodDays());

                    subscription=subscriptionService.save(subscriptionBuilder.withStatus(EnumSubscriptionStatus.InCart).build());

                    CartLineItemBuilder cartLineItemBuilder  = new CartLineItemBuilder();
                    CartLineItem cartLineItem=cartLineItemBuilder.forSubscription(subscription).build();
                    cartLineItem.setOrder(order);
                    cartLineItem=cartLineItemService.save(cartLineItem);

                    subscription.setCartLineItem(cartLineItem);
                    subscription =subscriptionService.save(subscription);

                    subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.AddedToCart);
                }
            }
        } catch (Exception e) {
            getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
            return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
        }

        Map dataMap = new HashMap();
        userDao.refresh(order);
        Set<CartLineItem> subscriptionCartLineItems=new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
        //null pointer here --> putting a null check
        if (subscription != null && subscription.getQty()>0) {
            if(priorSubscription!=null){
                dataMap.put("name", subscription.getProductVariant().getProduct().getName());
                dataMap.put("options", subscription.getProductVariant().getOptionsCommaSeparated());
                dataMap.put("qty", subscription.getQty());
                dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size()) + subscriptionCartLineItems.size());
                HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, " variant subscription was already added to your cart", dataMap);
                noCache();
                return new JsonResolution(healthkartResponse);
            }else {
                dataMap.put("name", subscription.getProductVariant().getProduct().getName()+" subscription");
                dataMap.put("options", subscription.getProductVariant().getOptionsCommaSeparated());
                dataMap.put("qty", subscription.getQty());
                dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size()) + subscriptionCartLineItems.size());
                HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, " Subscription has been added to cart", dataMap);
                noCache();
                return new JsonResolution(healthkartResponse);
            }
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Subscription has not been added to cart", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
    }

    @ValidationMethod(on = {"addSubscription"})
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
            Date currentTime=BaseUtils.getCurrentTimestamp();
            int oneDay= 24*60*60*1000;
            Long days=(subscription.getStartDate().getTime() - BaseUtils.getCurrentTimestamp().getTime())/oneDay;
            if(days<-2 || days>62){
                getContext().getValidationErrors().add("4",new SimpleError("Please enter a start date with in two months from current date"));
            }
            //to do validate startDate to be within two months from current date
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public SubscriptionProduct getSubscriptionProduct() {
        return subscriptionProduct;
    }

    public void setSubscriptionProduct(SubscriptionProduct subscriptionProduct) {
        this.subscriptionProduct = subscriptionProduct;
    }

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
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

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public UserCartDao getUserCartDao() {
        return userCartDao;
    }

    public void setUserCartDao(UserCartDao userCartDao) {
        this.userCartDao = userCartDao;
    }

    public SignupAction getSignupAction() {
        return signupAction;
    }

    public void setSignupAction(SignupAction signupAction) {
        this.signupAction = signupAction;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public SubscriptionProductService getSubscriptionProductService() {
        return subscriptionProductService;
    }

    public void setSubscriptionProductService(SubscriptionProductService subscriptionProductService) {
        this.subscriptionProductService = subscriptionProductService;
    }

    public SubscriptionLoggingService getSubscriptionLoggingService() {
        return subscriptionLoggingService;
    }

    public void setSubscriptionLoggingService(SubscriptionLoggingService subscriptionLoggingService) {
        this.subscriptionLoggingService = subscriptionLoggingService;
    }

    public CartLineItemService getCartLineItemService() {
        return cartLineItemService;
    }

    public void setCartLineItemService(CartLineItemService cartLineItemService) {
        this.cartLineItemService = cartLineItemService;
    }
}
