package com.hk.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hk.cache.RoleCache;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.discount.OfferConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.OfferManager;
import com.hk.pact.dao.offer.OfferDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.discount.CouponService;

/**
 * Created by IntelliJ IDEA. User: Ajeet Date: Nov 5, 2011 Time: 8:27:26 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class IHOManager {

    private static Logger    logger = LoggerFactory.getLogger(IHOManager.class);

    private UserService      userService;
    private CouponService    couponService;
    private RoleService      roleService;

    private OfferManager     offerManager;
    private OfferDao         offerDao;
    private OfferInstanceDao offerInstanceDao;

    public boolean isValidIHOCard(String cardNumber) {
        logger.debug("IHO Card Number: " + cardNumber);
        if (StringUtils.isNumeric(cardNumber) && cardNumber.length() == 16) { // Sample IHO Card Number -
            Long ihoNumber = Long.valueOf(cardNumber);
            logger.debug("ihoNumber: " + ihoNumber);
            if ((ihoNumber >= 2770111099084381L && ihoNumber <= 2770111099959999L) || (ihoNumber >= 2703111099960001L && ihoNumber <= 2703111099999999L)) {
                if (!cardNumber.endsWith("0000")) {
                    logger.debug("IHO card is valid. ");
                    return true;
                }
            }
        }
        return false;
    }

    public Coupon findIHOCouponOfUser(User user) {
        Coupon ihoCoupon = null;
        Offer ihoOffer = getOfferDao().findByIdentifier(OfferConstants.IHO_HK_Offer);
        List<OfferInstance> oiList = getOfferInstanceDao().findOfferInstancesByUserAndOffer(user, ihoOffer);
        if (oiList != null && !oiList.isEmpty()) {
            ihoCoupon = oiList.get(0).getCoupon();
        }
        return ihoCoupon;
    }

    public Coupon createIHOCoupon(User user, String code) {
        Coupon ihoCoupon = null;
        Role hkIHOUSer = RoleCache.getInstance().getRoleByName(RoleConstants.HK_IHO_USER).getRole();
        //if (isValidIHOCard(code) && !user.getRoles().contains(getRoleService().getRoleByName(RoleConstants.HK_IHO_USER))) {
        if (isValidIHOCard(code) && !user.getRoles().contains(hkIHOUSer)) {
            Offer offer = getOfferManager().getOfferForIHO();
            ihoCoupon = getCouponService().createCoupon(code, null, null, null, offer, null, true, null);
            //user.getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_IHO_USER));
            user.getRoles().add(hkIHOUSer);
            user = getUserService().save(user);
        }
        return ihoCoupon;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public CouponService getCouponService() {
        return couponService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public OfferManager getOfferManager() {
        return offerManager;
    }

    public void setOfferManager(OfferManager offerManager) {
        this.offerManager = offerManager;
    }

    public OfferDao getOfferDao() {
        return offerDao;
    }

    public void setOfferDao(OfferDao offerDao) {
        this.offerDao = offerDao;
    }

    public OfferInstanceDao getOfferInstanceDao() {
        return offerInstanceDao;
    }

    public void setOfferInstanceDao(OfferInstanceDao offerInstanceDao) {
        this.offerInstanceDao = offerInstanceDao;
    }

}
