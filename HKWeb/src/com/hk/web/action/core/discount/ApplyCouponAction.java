package com.hk.web.action.core.discount;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.domain.coupon.CouponType;
import net.sourceforge.stripes.action.*;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.testng.Assert;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Modal;
import com.hk.admin.manager.EmployeeManager;
import com.hk.admin.manager.IHOManager;
import com.hk.constants.discount.OfferConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferEmailDomain;
import com.hk.domain.offer.OfferTrigger;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.user.User;
import com.hk.domain.user.Role;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.HealthkartResponse;
import com.hk.dto.pricing.PricingDto;
import com.hk.util.OfferTriggerMatcher;
import com.hk.cache.RoleCache;

import javax.ws.rs.PathParam;

/**
 * User: rahul Time: 8 Jan, 2010 6:19:28 PM
 */
@Secure
@Modal
@Component
public class ApplyCouponAction extends BaseAction {

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

    @DefaultHandler
    public Resolution apply() {
        if (StringUtils.isBlank(couponCode)) {
            message = new LocalizableMessage("/ApplyCoupon.action.coupon.required").getMessage(getContext().getLocale());
            return new ForwardResolution("/pages/modal/applyCoupon.jsp");
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
        /*if (coupon == null) {
            coupon = employeeManager.createEmpCoupon(user, couponCode);
        }*/
        Role hkEmpRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_EMPLOYEE).getRole();


        if (coupon == null) {
            message = new LocalizableMessage("/ApplyCoupon.action.invalid.coupon").getMessage(getContext().getLocale());
        } else {
            List<OfferInstance> offerInstances = offerInstanceDao.findByUserAndCoupon(user, coupon);
            if (offerInstances != null && !offerInstances.isEmpty()) {
                offerInstance = offerInstances.get(0);
            }
            if (offerInstance != null && !coupon.getRepetitiveUsage()) {
                if (!offerInstance.isActive()) {
                    error = error_alreadyUsed;
                    message = new SimpleMessage("This offer has already been used").getMessage(getContext().getLocale());
                } else {
                    error = error_alreadyApplied;
                    order.setOfferInstance(offerInstance);
                    message = new LocalizableMessage("/ApplyCoupon.action.coupon.already.applied").getMessage(getContext().getLocale());
                }
            } else if (!coupon.isValid()) {
                message = new LocalizableMessage("/ApplyCoupon.action.expired.coupon").getMessage(getContext().getLocale());
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
		            message = new LocalizableMessage("/ApplyCoupon.action.offer.not.allowed").getMessage(getContext().getLocale());
	            }
            } else if (couponCode.equals(OfferConstants.HK_EMPLOYEE_CODE) && !user.getRoles().contains(hkEmpRole)) {
                error = error_role;
                message = new LocalizableMessage("/ApplyCoupon.action.offer.not.allowed").getMessage(getContext().getLocale());
            } else if (user.equals(coupon.getReferrerUser())) {
              message = new LocalizableMessage("/ApplyCoupon.action.same.user.referrel.coupon").getMessage(getContext().getLocale());
            } else if (coupon.getReferrerUser() != null && user.getReferredBy() != null) {
                error = error_alreadyReferrer;
                message = new LocalizableMessage("/ApplyCoupon.action.already.has.referrer").getMessage(getContext().getLocale());
            } else if (coupon.getReferrerUser() != null && coupon.getCouponType() != null && !coupon.getCouponType().getId().equals(EnumCouponType.AFFILIATE.getId())
                    && user.getCreateDate().before(coupon.getCreateDate())) {
                error = error_referralNotAllowed;
                message = new LocalizableMessage("/ApplyCoupon.action.referrer.coupon.not.allowed").getMessage(getContext().getLocale());
            } else if (coupon.getReferrerUser() != null && orderDao.getLatestOrderForUser(user) != null) {
                message = new LocalizableMessage("/ApplyCoupon.action.referrer.coupon.first.order").getMessage(getContext().getLocale());
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
                  freeVariant.setQty(1L);
                  orderManager.createLineItems(Arrays.asList(freeVariant), order, null, null, null);
                  message = "Offer variant successfuly added to your cart. Please <a href='javascript:location.reload();' style='font-size:1.2em;'>refresh</a> your cart.";
                } else {
                  message = "Oops! Offer is over.";
                  error = error_freeVariantStockOver;
                }
              } else {
                message = new LocalizableMessage("/ApplyCoupon.action.coupon.successfully.applied").getMessage(getContext().getLocale());
              }
            }
        }
        return new ForwardResolution("/pages/modal/applyCoupon.jsp");
    }



  public Resolution applyOffer() {
    User user = getUserService().getUserById(getPrincipal().getId());
    Order order = orderManager.getOrCreateOrder(user);
    List<OfferInstance> offerInstances = offerInstanceDao.findActiveOfferInstances(user, offer);
    if (!offerInstances.isEmpty()) {
      offerInstance = offerInstances.get(0);
    } else {
      offerInstance = offerInstanceDao.createOfferInstance(offer, null, user, offer.getEndDate());
    }
    order.setOfferInstance(offerInstance);
    orderDao.save(order);
    success = true;

    ProductVariant freeVariant = offer.getOfferAction().getFreeVariant();
    if (freeVariant != null) {
      if (!freeVariant.isDeleted() && !freeVariant.isOutOfStock()) {
        orderManager.createLineItems(Arrays.asList(freeVariant), order, null, null, null);
      }
    }

    return new RedirectResolution(CartAction.class);

  }

  public Resolution removeOffer() {
    User user = getUserService().getUserById(getPrincipal().getId());
    Order order = orderManager.getOrCreateOrder(user);
    order.setOfferInstance(null);
    orderDao.save(order);

    ProductVariant freeVariant = offer.getOfferAction().getFreeVariant();
    if (freeVariant != null) {
      if (!freeVariant.isDeleted() && !freeVariant.isOutOfStock()) {
        CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(freeVariant).addCartLineItemType(EnumCartLineItemType.Product);
        CartLineItem cartLineItem = cartLineItemService.getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);
        if(cartLineItem != null){
          cartLineItem.setQty(0L);
          cartLineItemService.save(cartLineItem);
        }
      }
    }

    return new RedirectResolution(CartAction.class);
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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
}
