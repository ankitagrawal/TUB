package web.action.core.discount;

import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

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
import com.hk.dao.BaseDao;
import com.hk.dao.coupon.CouponDao;
import com.hk.dao.offer.OfferInstanceDao;
import com.hk.dao.order.OrderDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.service.UserService;

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

    private OfferInstance      offerInstance;

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
        if (coupon == null) {
            coupon = employeeManager.createEmpCoupon(user, couponCode);
        }

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
            } else if (!offerManager.isOfferValidForUser(coupon.getOffer(), user)) {
                error = error_role;
                message = new LocalizableMessage("/ApplyCoupon.action.offer.not.allowed").getMessage(getContext().getLocale());
            } else if (user.equals(coupon.getReferrerUser())) {
                message = new LocalizableMessage("/ApplyCoupon.action.same.user.referrel.coupon").getMessage(getContext().getLocale());
            } else if (coupon.getReferrerUser() != null && user.getReferredBy() != null) {
                error = error_alreadyReferrer;
                message = new LocalizableMessage("/ApplyCoupon.action.already.has.referrer").getMessage(getContext().getLocale());
            } else if (coupon.getReferrerUser() != null && !coupon.getOffer().getOfferIdentifier().equals(OfferConstants.affiliateCommissionOffer)
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
                    if (coupon.getOffer().getOfferIdentifier().equals(OfferConstants.affiliateCommissionOffer)) {
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

                message = new LocalizableMessage("/ApplyCoupon.action.coupon.successfully.applied").getMessage(getContext().getLocale());
            }
        }

        return new ForwardResolution("/pages/modal/applyCoupon.jsp");
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

}
