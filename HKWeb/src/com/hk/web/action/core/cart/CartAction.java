package com.hk.web.action.core.cart;

import java.util.*;

import com.hk.constants.order.EnumOrderStatus;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.discount.OfferConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.SubscriptionFilter;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.store.EnumStore;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pact.service.order.OrderService;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.order.OrderSummaryAction;
import com.hk.web.action.core.user.SelectAddressAction;

@Component
@HttpCache(allow = false)
public class CartAction extends BaseAction {

    @SuppressWarnings("unused")
    private static Logger       logger        = LoggerFactory.getLogger(CartAction.class);

    @Validate(encrypted = true)
    private Order               order;
    // private List<CartLineItem> cartLineItems;
    private List<ComboInstance> comboInstances;
    private PricingDto          pricingDto;
    private Long                itemsInCart   = 0L;
    private String              freebieBanner;
    private Set<Subscription>   subscriptions;
    private Set<CartLineItem>   trimCartLineItems;
    private Integer               sizeOfCLI;

    @Autowired
    private UserService         userService;
    @Autowired
    AffiliateDao                affiliateDao;
    @Autowired
    UserManager                 userManager;
    @Autowired
    PricingEngine               pricingEngine;
    @Autowired
    OrderManager                orderManager;
    @Autowired
    LineItemDao                 lineItemDao;
    @Autowired
    CouponDao                   couponDao;
    @Autowired
    OfferInstanceDao            offerInstanceDao;
    @Autowired
    OfferManager                offerManager;
    @Autowired
    private OrderDao            orderDao;
    @Autowired
    private OrderService        orderService;
    @Autowired
    private CartFreebieService  cartFreebieService;

    boolean                     verifyMessage = false;

    @DefaultHandler
    public Resolution pre() {

        User user = getPrincipalUser();
        if (user == null) {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }
        order = orderManager.getOrCreateOrder(user);
        Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

        trimCartLineItems = orderManager.trimEmptyLineItems(order);
        sizeOfCLI = order.getCartLineItems().size();
        if (order != null && cartLineItems != null) {
            itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size());
        }

            /* Check if user is referred and has referral coupon to apply. If yes, apply automatically */
        if (user.getReferredBy() != null) {
            Coupon coupon = (user.getReferredBy()).getReferrerCoupon();
            if (coupon != null && coupon.isValid()) {
                List<OfferInstance> offerInstances = offerInstanceDao.findByUserAndCoupon(user, coupon);
                if (offerInstances == null || offerInstances.isEmpty()) {
                    if (offerManager.isOfferValidForUser(coupon.getOffer(), user)) {
                        Date offerInstanceEndDate = new DateTime().plusDays(OfferConstants.MAX_ALLOWED_DAYS_FOR_15_PERCENT_REFERREL_DISCOUNT).toDate();
                        OfferInstance offerInstance = offerInstanceDao.createOfferInstance(coupon.getOffer(), coupon, user, offerInstanceEndDate);
                        order.setOfferInstance(offerInstance);
                        coupon.setAlreadyUsed(coupon.getAlreadyUsed() + 1);
                        couponDao.save(coupon);
                    } else {
                        verifyMessage = true;
                    }
                }
            }
        }

        if (order.getOfferInstance() != null && !order.getOfferInstance().isValid()) {
            offerInstanceDao.save(order.getOfferInstance());
            order.setOfferInstance(null);
            order = orderDao.save(order);
        }

        Address address = order.getAddress() != null ? order.getAddress() : new Address();
        pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);

        Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
        if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
            subscriptions = new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter();
            itemsInCart += subscriptions.size();
        }
        freebieBanner = cartFreebieService.getFreebieBanner(order);
        if (isHybridRelease()) {
          return new ForwardResolution("/pages/cartBeta.jsp");
        }
        return new ForwardResolution("/pages/cart.jsp");
    }

    @DontValidate
    public Resolution getCartItems() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            // user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
        }
        if (user != null) {
            order = orderService.findCart(user, EnumStore.HEALTHKART.asStore());
            if (order != null) {
                Set<CartLineItem> cartLineItems = order.getCartLineItems();
                if (cartLineItems != null && !cartLineItems.isEmpty()) {
                    Set<CartLineItem> productCartLineItems = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Product).filter();
                    if (order != null && productCartLineItems != null) {
                        itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size());
                    }
                }
                int inCartSubscriptions = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Subscription).filter().size();
                itemsInCart += inCartSubscriptions;
            }
        }
        return new ForwardResolution("/pages/cart.jsp");
    }

    /**
     * method used to update the latest pricing, for eg when an offer is applied/changed
     *
     * @return
     */
    @JsonHandler
    @DontValidate
    public Resolution pricing() {
        pre();

        noCache();
        PricingSubDto pricingSubDto = new PricingSubDto(pricingDto, null);
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", pricingSubDto);
        return new JsonResolution(healthkartResponse);
    }

    public Resolution checkout() {
//        orderManager.trimEmptyLineItems(order);
        return new RedirectResolution(SelectAddressAction.class);
    }

    public Resolution removeGroundShippedItem() {
        orderManager.setGroundShippedItemQuantity(order);
        orderManager.trimEmptyLineItems(order);
        return new RedirectResolution(OrderSummaryAction.class);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getFreebieBanner() {
        return freebieBanner;
    }

    /*
     * public List<LineItem> getCartLineItems() { return cartLineItems; } public void setLineItems(List<LineItem>
     * cartLineItems) { this.cartLineItems = cartLineItems; }
     */

    public PricingDto getPricingDto() {
        return pricingDto;
    }

    public boolean isVerifyMessage() {
        return verifyMessage;
    }

    public Long getItemsInCart() {
        return itemsInCart;
    }

    public void setItemsInCart(Long itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public List<ComboInstance> getComboInstances() {
        return comboInstances;
    }

    public void setComboInstances(List<ComboInstance> comboInstances) {
        this.comboInstances = comboInstances;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

  public Set<CartLineItem> getTrimCartLineItems() {
    return trimCartLineItems;
  }

  public void setTrimCartLineItems(Set<CartLineItem> trimCartLineItems) {
    this.trimCartLineItems = trimCartLineItems;
  }

  public Integer getSizeOfCLI() {
    return sizeOfCLI;
  }

  public void setSizeOfCLI(Integer sizeOfCLI) {
    this.sizeOfCLI = sizeOfCLI;
  }
}