package com.hk.manager;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.DateUtils;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.discount.OfferConstants;
import com.hk.domain.builder.OfferActionBuilder;
import com.hk.domain.builder.OfferBuilder;
import com.hk.domain.builder.OfferTriggerBuilder;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferTrigger;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.offer.OfferDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.discount.CouponService;

@Component
public class OfferManager {

    @Autowired
    OfferDao         offerDao;

    @Autowired
    ProductService   productService;
    
    
    OfferInstanceDao offerInstanceDaoProvider;
    
    @Autowired
    RoleDao          roleDao;
    
    @Autowired
    CouponService    couponService;

    public boolean isOfferValidForUser(Offer offer, User user) {
        if (offer.getRoles().size() == 0) {
            return true;
        }
        for (Role role : user.getRoles()) {
            if (offer.getRoles().contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Offer getOfferForReferrelProgram() {
        Offer offer = getOfferDao().findByIdentifier(OfferConstants.referralOffer100100);

        if (offer == null) {

            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("Min purchase amount Rs 500").minAmount(500D).build();

            offerTrigger = (OfferTrigger) getOfferDao().save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("Rs. 100 off").discountAmount(100D).discountOnBasePriceOnly(false).validOnIntlShipping(false).qty(
                    1L).productGroup(getProductService().findProductGroupByName(OfferConstants.productGroupExcludingServices)).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("Rs. 100 off on purchase of 500 or more, referrer program discount").startDate(new DateTime().toDate()).numberOfTimesAllowed(
                    1L).carryOverAllowed(false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.referralOffer100100).offerTrigger(offerTrigger).offerAction(
                    offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            roles.add(getRoleDao().getRoleByName(RoleConstants.HK_USER));
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

        return offer;
    }

    @Transactional
    public Offer getOfferForReferrelProgram2() {
        Offer offer = offerDao.findByIdentifier(OfferConstants.offer15percentOffOnPurchaseOf250OrMore);

        if (offer == null) {

            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("Min purchase amount Rs 200").minAmount(200D).build();

            offerTrigger = (OfferTrigger) offerDao.save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("15% off").discountPercent(.15D).discountOnBasePriceOnly(false).validOnIntlShipping(false).qty(
                    1L).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("15% off on purchase of 200 or more, referrer program discount").startDate(new DateTime().toDate()).numberOfTimesAllowed(
                    1L).carryOverAllowed(false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.offer15percentOffOnPurchaseOf250OrMore).offerTrigger(offerTrigger).offerAction(
                    offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            roles.add((Role) getRoleDao().find(RoleConstants.HK_USER));
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

        return offer;
    }

    @Transactional
    public Offer getAffiliateOffer() {
        Offer offer = getOfferDao().findByIdentifier(OfferConstants.affiliateCommissionOffer);

        if (offer == null) {

            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("Min purchase amount Rs 500").minAmount(500D).build();

            offerTrigger = (OfferTrigger) offerDao.save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("Rs. 100 off").discountAmount(100D).discountOnBasePriceOnly(false).validOnIntlShipping(false).productGroup(
                    getProductService().findProductGroupByName(OfferConstants.productGroupExcludingServices)).qty(1L).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("Rs. 100 off on purchase of 500 or more, affiliate program discount").startDate(new DateTime().toDate()).numberOfTimesAllowed(
                    1L).carryOverAllowed(false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.affiliateCommissionOffer).offerTrigger(offerTrigger).offerAction(
                    offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            roles.add((Role) getRoleDao().find(RoleConstants.HK_USER));
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

        return offer;
    }

    @Transactional
    public Offer getDiwaliOfferAndCreateCoupon() {
        Offer offer = getOfferDao().findByIdentifier(OfferConstants.diwaliOffer);

        if (offer == null) {

            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("Min purchase amount Rs 10").minAmount(10D).build();

            offerTrigger = (OfferTrigger) offerDao.save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("20% Cashback Special Diwali Dhamaka off").discountAmount(0D).discountOnBasePriceOnly(false).validOnIntlShipping(
                    false).qty(1L).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("20% Cashback Special Diwali Dhamaka off").startDate(new DateTime().toDate()).numberOfTimesAllowed(1L).carryOverAllowed(
                    false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.diwaliOffer).offerTrigger(offerTrigger).offerAction(offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            roles.add((Role) getRoleDao().find(RoleConstants.HK_USER));
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

        Coupon diwaliCoupon = getCouponService().createCoupon("DIWALIDHAMAKA", DateUtils.getEndOfNextDay(new Date()), 500L, 0L, offer, null, false);

        return offer;
    }

    @Transactional
    public Offer getOfferForIHO() {
        Offer offer = getOfferDao().findByIdentifier(OfferConstants.IHO_HK_Offer);
        if (offer == null) {
            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("IHO Card Holder").minAmount(10D).build();

            offerTrigger = (OfferTrigger) getOfferDao().save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("Additional 5% Off for IHO card holders").discountPercent(0.05D).discountOnBasePriceOnly(false).validOnIntlShipping(
                    false).qty(1L).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("Additional 5% Off for IHO card holders.").startDate(new DateTime().toDate()).numberOfTimesAllowed(9999L).carryOverAllowed(
                    false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.IHO_HK_Offer).offerTrigger(offerTrigger).offerAction(offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            roles.add((Role) getRoleDao().find(RoleConstants.HK_IHO_USER));
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

        return offer;
    }

    @Transactional
    public Offer getOfferForEmployee() {
        Offer offer = getOfferDao().findByIdentifier(OfferConstants.HK_EMPLOYEE_OFFER);
        if (offer == null) {
            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("Employee Offer").minAmount(10D).build();

            offerTrigger = (OfferTrigger) getOfferDao().save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("Employee Offer, procuring at cost price itself, Free Shipping, COD").discountPercent(0.0D).discountOnBasePriceOnly(
                    false).validOnIntlShipping(false).qty(1L).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("Employee Offer, procuring at cost price itself, Free Shipping, COD.").startDate(new DateTime().toDate()).numberOfTimesAllowed(
                    9999L).carryOverAllowed(false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.HK_EMPLOYEE_OFFER).offerTrigger(offerTrigger).offerAction(
                    offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            roles.add((Role) getRoleDao().find(RoleConstants.HK_EMPLOYEE));
            offer.setRoles(roles);

            offer = (Offer) offerDao.save(offer);
        }

        return offer;
    }

    public OfferDao getOfferDao() {
        return offerDao;
    }

    public void setOfferDao(OfferDao offerDao) {
        this.offerDao = offerDao;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public CouponService getCouponService() {
        return couponService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }
    
    

}
