package com.hk.web.action.admin.offer;

import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.DateUtils;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.user.User;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA. User: USER Date: Sep 3, 2011 Time: 5:10:59 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class EditCouponAction extends BaseAction {
    String              code;
    Long                allowedTimes;
    Coupon              coupon;
    Date                endDate;
    User                user;
    private Boolean     repetitiveUsage;

    List<OfferInstance> offerInstanceList;
    @Autowired
    CouponDao           couponDao;
    @Autowired
    OfferInstanceDao    offerInstanceDao;
    @Autowired
    UserDao             userDao;

    @DefaultHandler
    @DontValidate
    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_COUPON }, authActionBean = AdminPermissionAction.class)
    public Resolution find() {
        coupon = couponDao.findByCode(code);
        return new ForwardResolution("/pages/admin/offer/editCoupon.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_COUPON }, authActionBean = AdminPermissionAction.class)
    public Resolution findOfferInstanceListByCoupon() {
        coupon = couponDao.findByCode(code);
        offerInstanceList = offerInstanceDao.findByCoupon(coupon);
        for (OfferInstance offerInstance : offerInstanceList) {
            if (!offerInstance.isValid())
                offerInstanceDao.save(offerInstance);
        }
        return new ForwardResolution("/pages/admin/offer/editCoupon.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_COUPON }, authActionBean = AdminPermissionAction.class)
    public Resolution save() {
        coupon = couponDao.findByCode(code);
        coupon.setEndDate(DateUtils.getEndOfDay(endDate));
        coupon.setAllowedTimes(allowedTimes);
        if (repetitiveUsage == null)
            repetitiveUsage = false;
        coupon.setRepetitiveUsage(repetitiveUsage);
        couponDao.save(coupon);
        addRedirectAlertMessage(new SimpleMessage("saved sucessfully"));
        return new ForwardResolution("/pages/admin/offer/editCoupon.jsp");
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getAllowedTimes() {
        return allowedTimes;
    }

    public void setAllowedTimes(Long allowedTimes) {
        this.allowedTimes = allowedTimes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OfferInstance> getOfferInstanceList() {
        return offerInstanceList;
    }

    public void setOfferInstanceList(List<OfferInstance> offerInstanceList) {
        this.offerInstanceList = offerInstanceList;
    }

    public Boolean isRepetitiveUsage() {
        return repetitiveUsage;
    }

    public void setRepetitiveUsage(Boolean repetitiveUsage) {
        this.repetitiveUsage = repetitiveUsage;
    }
}
