package com.hk.api.resource;

import javax.ws.rs.*;

import com.akube.framework.dao.Page;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.SubscriptionFilter;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.coupon.CouponType;
import com.hk.domain.offer.OfferEmailDomain;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.Address;
import com.hk.manager.UserManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.offer.OfferDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pricing.PricingEngine;
import com.hk.util.json.JSONResponseBuilder;
import com.shiro.PrincipalImpl;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;

import org.joda.time.DateTime;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.EmployeeManager;
import com.hk.admin.manager.IHOManager;
import com.hk.constants.discount.OfferConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.offer.Offer;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.user.User;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.dto.pricing.PricingDto;
import com.hk.util.OfferTriggerMatcher;
import org.testng.Assert;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


@Path ("/cartResource")
@Component
public class CartResource extends BaseAction {

    @Autowired
    private BaseDao            baseDao;
    @Autowired
    private UserService        userService;
    @Autowired
    private CouponDao          couponDao;
    @Autowired
    private OfferInstanceDao   offerInstanceDao;
    @Autowired
    private OrderDao           orderDao;
    @Autowired
    private OrderManager       orderManager;
    @Autowired
    private OfferManager       offerManager;
    @Autowired
    private IHOManager         ihoManager;
    @Autowired
    private EmployeeManager    employeeManager;
    @Autowired
    private CartLineItemService cartLineItemService;

    private String             couponCode;

    private Coupon             coupon;
    private String             message;
    private boolean            success                  = false;

    private String             error;
    public static final String error_role               = "error_role";
    public static final String error_alreadyUsed        = "error_alreadyUsed";
    public static final String error_alreadyApplied     = "error_alreadyApplied";
    public static final String error_alreadyReferrer    = "error_alreadyReferrer";
    public static final String error_referralNotAllowed = "error_referralNotAllowed";
    public static final String error_couponExpired      = "error_couponExpired";
    public static final String error_freeVariantStockOver    = "error_freeVariantStockOver";

    private OfferInstance      offerInstance;
    private Offer      offer;
    @Validate(encrypted = true)
    private Order order;
    // private List<CartLineItem> cartLineItems;
    private List<ComboInstance> comboInstances;
    private PricingDto pricingDto;
    private Long itemsInCart = 0L;
    private String freebieBanner;
    private Set<Subscription> subscriptions;

    @Autowired
    AffiliateDao affiliateDao;
    @Autowired
    UserManager userManager;
    @Autowired
    PricingEngine pricingEngine;
    @Autowired
    LineItemDao lineItemDao;
    @Autowired
    private CartFreebieService cartFreebieService;
    @Autowired
    OfferDao offerDao;

    boolean verifyMessage = false;

    List<Offer> applicableOffers = new ArrayList<Offer>();

  private static Logger logger = LoggerFactory.getLogger(CartResource.class);

    @GET
    @Path("/applyCoupon")
    @Produces("application/json")
    public String applyCoupon(@QueryParam ("couponCode") String couponCode) {
        if (StringUtils.isBlank(couponCode)) {
            message = "Please enter a coupon code.";
            return message;
        }

        coupon = couponDao.findByCode(couponCode);
        User user = getUserService().getUserById(getPrincipal().getId());
        Order order = orderManager.getOrCreateOrder(user);

        // If coupon is NULL
        // Check if the user has applied an IHO Coupon for the first time
        // and create the coupon - it will validate and then create coupon
        if (coupon == null) {
            coupon = ihoManager.createIHOCoupon(user, couponCode);
        }
        if (coupon == null) {
            coupon = employeeManager.createEmpCoupon(user, couponCode);
        }

        if (coupon == null) {
            message = "Coupon code is invalid.";
        } else {
            List<OfferInstance> offerInstances = offerInstanceDao.findByUserAndCoupon(user, coupon);
            if (offerInstances != null && !offerInstances.isEmpty()) {
                offerInstance = offerInstances.get(0);
            }
            if (offerInstance != null && !coupon.getRepetitiveUsage()) {
                if (!offerInstance.isActive()) {
                    error = error_alreadyUsed;
                    message = "This offer has already been used";
                } else {
                    error = error_alreadyApplied;
                    order.setOfferInstance(offerInstance);
                    message = "You have already added this coupon.";
                }
            } else if (!coupon.isValid()) {
                message = "Coupon code has expired.";
                error = error_couponExpired;
            } else if (!offerManager.isOfferValidForUser(coupon.getOffer(), user)) {
                error = error_role;
                Offer offer = coupon.getOffer();
                if (offer.getOfferEmailDomains().size() > 0) {
                    message = "The offer is valid for the following domains only:";
                    for (OfferEmailDomain offerEmailDomain : offer.getOfferEmailDomains()) {
                        message += "<br/>" + offerEmailDomain.getEmailDomain();
                    }
                } else {
                    message = "This offer is not activated for you yet.";
                }
            } else if (user.equals(coupon.getReferrerUser())) {
                message = "You are not allowed to use your own referrer code.";
            } else if (coupon.getReferrerUser() != null && user.getReferredBy() != null) {
                error = error_alreadyReferrer;
                message = "You have already mentioned your referrer.";
            } else if (coupon.getReferrerUser() != null && coupon.getCouponType() != null && !coupon.getCouponType().getId().equals(EnumCouponType.AFFILIATE.getId())
                    && user.getCreateDate().before(coupon.getCreateDate())) {
                error = error_referralNotAllowed;
                message = "You are not allowed to use this referrer coupon.";
            } else if (coupon.getReferrerUser() != null && orderDao.getLatestOrderForUser(user) != null) {
                message = "Coupon can not be applied. This is a referral discount coupon and it is only valid before you place your first order.";
            } else {

                Date offerInstanceEndDate = null;

                // add referredBy to the user if coupon contains the referrerUser
                if (coupon.getReferrerUser() != null) {
                    // add affiliate_to to the user if its an affiliate coupon
                    CouponType couponType = coupon.getCouponType();
                    if (couponType != null && couponType.getId().equals(EnumCouponType.AFFILIATE.getId())) {
                        Assert.assertNull(user.getAffiliateTo());
                        user.setAffiliateTo(coupon.getReferrerUser());
                        user = (User) getBaseDao().save(user);
                    } else { // its a referral coupon
                        Assert.assertNull(user.getReferredBy());
                        user.setReferredBy(coupon.getReferrerUser());
                        user = (User) getBaseDao().save(user);
                        offerInstanceEndDate = new DateTime().plusDays(OfferConstants.MAX_ALLOWED_DAYS_FOR_15_PERCENT_REFERREL_DISCOUNT).toDate();
                    }
                }
                if (coupon.getRepetitiveUsage()) {
                    List<OfferInstance> activeOfferInstances = offerInstanceDao.findActiveOfferInstances(user, coupon.getOffer());
                    if (activeOfferInstances == null || activeOfferInstances.isEmpty()) {
                        offerInstance = offerInstanceDao.createOfferInstance(coupon.getOffer(), coupon, user, offerInstanceEndDate);
                    }
                } else {
                    offerInstance = offerInstanceDao.createOfferInstance(coupon.getOffer(), coupon, user, offerInstanceEndDate);
                }
                order.setOfferInstance(offerInstance);
                coupon.setAlreadyUsed(coupon.getAlreadyUsed() + 1);
                couponDao.save(coupon);
                success = true;

                ProductVariant freeVariant = coupon.getOffer().getOfferAction().getFreeVariant();
                if (freeVariant != null) {
                    //OfferTriggerMatcher offerTriggerMatcher = new OfferTriggerMatcher(coupon.getOffer().getOfferTrigger(), order.getCartLineItems());
                    //&& offerTriggerMatcher.hasEasyMatch(false)
                    if (!freeVariant.isDeleted() && !freeVariant.isOutOfStock()) {
                        orderManager.createLineItems(Arrays.asList(freeVariant), order, null, null, null);
                        message = "Free variant successfuly added to your cart. Please <a href='javascript:location.reload();' style='font-size:1.2em;'>refresh</a> your cart.";
                    } else {
                        message = "Oops! Offer is over.";
                        error = error_freeVariantStockOver;
                    }
                } else {
                    message = "Coupon applied successfully.";
                }
            }
        }
        return new JSONResponseBuilder().addField("offerInstance",offerInstance).addField("error", error).build();
    }

    @GET
    @Path("/otherApplicableOffers")
    @Produces("application/json")
    public String otherApplicableOffers(){
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            // user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }
        if (user != null) {
            order = orderManager.getOrCreateOrder(user);
            Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            Set<Long> comboInstanceIds = new TreeSet<Long>();
            for (CartLineItem lineItem : cartLineItems) {
                if (lineItem != null && lineItem.getProductVariant() != null) {
                    ProductVariant productVariant = lineItem.getProductVariant();
                    if ((productVariant.getProduct().isDeleted() != null && productVariant.getProduct().isDeleted()) || productVariant.isDeleted() || productVariant.isOutOfStock()) {
                        lineItem.setQty(0L);
                        if (lineItem.getComboInstance() != null) {
                            comboInstanceIds.add(lineItem.getComboInstance().getId());
                        }
                    }
                }
            }
            for (Long comboInstanceId : comboInstanceIds) {
                for (CartLineItem cartLineItem : cartLineItems) {
                    if (cartLineItem.getComboInstance() != null && cartLineItem.getComboInstance().getId().equals(comboInstanceId)) {
                        cartLineItem.setQty(0L);
                    }
                }
            }
            // Trimming cart line items in case of zero qty ie deleted/outofstock/removed
            orderManager.trimEmptyLineItems(order);

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
            pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);

            Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
            if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
                subscriptions = new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter();
                itemsInCart += subscriptions.size();
            }
        }

        freebieBanner = cartFreebieService.getFreebieBanner(order);

        //Cart Listener at work - will get applicable offers.
        applicableOffers = this.getApplicableOffers(order);
        Offer appliedOffer = null;
        if(order.getOfferInstance()!=null){
            appliedOffer = order.getOfferInstance().getOffer();
        }

        return new JSONResponseBuilder().addField("applicableOffers",applicableOffers).addField("appliedOffer",appliedOffer).build();
    }

    @GET
    @Path("/roles")
    @Produces("application/json")
    public String getUserRoles() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        return new JSONResponseBuilder().addField("roles", user.getRoles()).build();
    }

    @Override
    public PrincipalImpl getPrincipal() {
        return super.getPrincipal();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public OfferInstance getOfferInstance() {
        return offerInstance;
    }


    public CouponDao getCouponDao() {
        return couponDao;
    }

    public void setCouponDao(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public OfferInstanceDao getOfferInstanceDao() {
        return offerInstanceDao;
    }

    public void setOfferInstanceDao(OfferInstanceDao offerInstanceDao) {
        this.offerInstanceDao = offerInstanceDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public OfferManager getOfferManager() {
        return offerManager;
    }

    public void setOfferManager(OfferManager offerManager) {
        this.offerManager = offerManager;
    }

    public IHOManager getIhoManager() {
        return ihoManager;
    }

    public void setIhoManager(IHOManager ihoManager) {
        this.ihoManager = ihoManager;
    }

    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
    public List<Offer> getApplicableOffers(Order order) {
        List<Offer> applicableOffers = new ArrayList<Offer>();
        Page activeOffersPage = offerDao.listAllValid(1, 5);
        if (activeOffersPage != null) {
            List<Offer> activeOffers = activeOffersPage.getList();
            for (Offer activeOffer : activeOffers) {
              if (activeOffer.getOfferTrigger() != null) {
                logger.debug("Active Offer ID -> " + activeOffer.getId());
                OfferTriggerMatcher offerTriggerMatcher = new OfferTriggerMatcher(activeOffer.getOfferTrigger(), order.getCartLineItems());
                if (offerTriggerMatcher.hasEasyMatch(false) && offerManager.isOfferValidForUser(activeOffer, order.getUser()) && activeOffer.isShowPromptly()) {
                  if (activeOffer.getOfferAction().getFreeVariant() != null) {
                    ProductVariant freeVariant = activeOffer.getOfferAction().getFreeVariant();
                    if (!freeVariant.isDeleted() && !freeVariant.isOutOfStock()) {
                      applicableOffers.add(activeOffer);
                    }
                  } else {
                    applicableOffers.add(activeOffer);
                  }
                }
              }
            }
        }
        return applicableOffers;
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

    public List<Offer> getApplicableOffers() {
        return applicableOffers;
    }
}
