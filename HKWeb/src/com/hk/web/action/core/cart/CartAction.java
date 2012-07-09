package com.hk.web.action.core.cart;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.fliter.SubscriptionFilter;
import com.hk.domain.subscription.Subscription;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
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
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
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
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.user.SelectAddressAction;

@Component
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
    private Set<Subscription> subscriptions;

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
    private CartFreebieService  cartFreebieService;

    boolean                     verifyMessage = false;

    @DefaultHandler
    public Resolution pre() {
        // TODO: # warehouse fix this.

        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }
        if (user != null) {
            order = orderManager.getOrCreateOrder(user);

            Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            for (CartLineItem lineItem : cartLineItems) {
                if (lineItem != null && lineItem.getProductVariant() != null) {
                    ProductVariant productVariant = lineItem.getProductVariant();
                    if ((productVariant.getProduct().isDeleted() != null && productVariant.getProduct().isDeleted()) || productVariant.isDeleted() || productVariant.isOutOfStock()) {
                        lineItem.setQty(0L);
                    }
                }
            }

            // Trimming cart line items in case of zero qty ie deleted/outofstock/removed
            order = orderManager.trimEmptyLineItems(order);

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
            /*
             * Set<CartLineItem> cartLineItemsSet = new HashSet<CartLineItem>();
             * cartLineItemsSet.addAll(cartLineItems);
             */
            subscriptions = new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter();
            if(subscriptions !=null && subscriptions.size()>0){
                pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D, subscriptions), address);
                 itemsInCart+=subscriptions.size();

            }else {
                pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);
            }
        }

        freebieBanner = cartFreebieService.getFreebieBanner(order);
        return new ForwardResolution("/pages/cart.jsp");
    }

    @DontValidate
    public Resolution getCartItems() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        if (user != null) {
            order = orderDao.findByUserAndOrderStatus(user, EnumOrderStatus.InCart);
            if (order != null) {
                Set<CartLineItem> cartLineItems = order.getCartLineItems();
                if (cartLineItems != null && !cartLineItems.isEmpty()) {
                    Set<CartLineItem> productCartLineItems = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Product).filter();
                    if (order != null && productCartLineItems != null) {
                        itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size());
                    }
                }
               Set<Subscription> inCartSubscriptions= new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter();
               if(inCartSubscriptions!=null && inCartSubscriptions.size()>0){
                 itemsInCart+=inCartSubscriptions.size();
               }
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
        orderManager.trimEmptyLineItems(order);
        /*
         * for (Iterator<LineItem> lineItemIterator = order.getProductCartLineItems().iterator();
         * lineItemIterator.hasNext();) { LineItem lineItem = lineItemIterator.next(); lineItemDao.save(lineItem); }
         */

        return new RedirectResolution(SelectAddressAction.class);
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
}