package com.hk.api.resource;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.EmployeeManager;
import com.hk.admin.manager.IHOManager;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.constants.discount.OfferConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.coupon.CouponType;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferEmailDomain;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.Role;
import com.hk.domain.store.EnumStore;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.offer.OfferDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderService;
import com.hk.util.OfferTriggerMatcher;
import com.hk.util.json.JSONResponseBuilder;
import com.hk.cache.RoleCache;
import com.shiro.PrincipalImpl;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.action.LocalizableMessage;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.*;


@Path("/cartResource")
@Component
public class CartResource extends BaseAction {

  @Autowired
  private BaseDao baseDao;
  @Autowired
  private UserService userService;
  @Autowired
  private CouponDao couponDao;
  @Autowired
  private OfferInstanceDao offerInstanceDao;
  @Autowired
  private OrderDao orderDao;
  @Autowired
  private OrderService orderService;
  @Autowired
  private OrderManager orderManager;
  @Autowired
  private OfferManager offerManager;
  @Autowired
  private IHOManager ihoManager;
  @Autowired
  private EmployeeManager employeeManager;

  private String couponCode;

  private Coupon coupon;
  private String message;
  private boolean success = false;

  private String error;
  public static final String error_role = "error_role";
  public static final String error_alreadyUsed = "error_alreadyUsed";
  public static final String error_alreadyApplied = "error_alreadyApplied";
  public static final String error_alreadyReferrer = "error_alreadyReferrer";
  public static final String error_referralNotAllowed = "error_referralNotAllowed";
  public static final String error_couponExpired = "error_couponExpired";
  public static final String error_freeVariantStockOver = "error_freeVariantStockOver";

  private OfferInstance offerInstance;
  @Validate(encrypted = true)
  private Order order;

  @Autowired
  UserManager userManager;
  @Autowired
  OfferDao offerDao;

  boolean verifyMessage = false;

  Set<Offer> applicableOffers = new HashSet<Offer>();

  private static Logger logger = LoggerFactory.getLogger(CartResource.class);

  @GET
  @Path("/applyCoupon")
  @Produces("application/json")
  public String applyCoupon(@QueryParam("couponCode") String couponCode) {
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
    /*if (coupon == null) {
      coupon = employeeManager.createEmpCoupon(user, couponCode);
    }*/

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
      } else if (couponCode.equals(OfferConstants.HK_EMPLOYEE_CODE)) {
        Role hkEmpRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_EMPLOYEE).getRole();
        if (!user.getRoles().contains(hkEmpRole)) {
          error = error_role;
          message = new LocalizableMessage("/ApplyCoupon.action.offer.not.allowed").getMessage(getContext().getLocale());
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
    return new JSONResponseBuilder().addField("offerInstance", offerInstance).addField("error", error).build();
  }

  @GET
  @Path("/otherApplicableOffers")
  @Produces("application/json")
  public String otherApplicableOffers() {
    Offer appliedOffer = null;
    if (getPrincipal() != null) {
      User user = getUserService().getUserById(getPrincipal().getId());
      if (user != null) {
        order = getOrderService().findCart(user, EnumStore.HEALTHKART.asStore());
        if (order != null) {
          if (order.getOfferInstance() != null) {
            appliedOffer = order.getOfferInstance().getOffer();
          }
          applicableOffers = this.getApplicableOffers(order);
        }
      }
    }
    return new JSONResponseBuilder().addField("applicableOffers", applicableOffers).addField("appliedOffer", appliedOffer).build();
  }

  public Set<Offer> getApplicableOffers(Order order) {
    applicableOffers = new HashSet<Offer>();
    User user = order.getUser();
      List<Offer> activeOffers = offerDao.listAllValidShowPromptly();
      if (activeOffers != null) {
        for (Offer activeOffer : activeOffers) {
          if (activeOffer.getOfferTrigger() != null) {
            logger.debug("Active Offer ID -> " + activeOffer.getId());
            OfferTriggerMatcher offerTriggerMatcher = new OfferTriggerMatcher(activeOffer.getOfferTrigger(), order.getCartLineItems());
            if (offerTriggerMatcher.hasEasyMatch(false) && offerManager.isOfferValidForUser(activeOffer, user) && activeOffer.isShowPromptly()) {
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
      List<OfferInstance> offerInstances = offerInstanceDao.getActiveOffers(user);
      for (OfferInstance instance : offerInstances) {
        if (offerManager.isOfferValidForUser(instance.getOffer(), user)) {
          applicableOffers.add(instance.getOffer());
        }
      }
    return applicableOffers;
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


  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }


  public boolean isVerifyMessage() {
    return verifyMessage;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public OrderService getOrderService() {
    return orderService;
  }

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  public Set<Offer> getApplicableOffers() {
    return applicableOffers;
  }
}
