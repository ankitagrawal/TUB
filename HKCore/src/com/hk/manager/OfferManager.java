package com.hk.manager;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hk.cache.RoleCache;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.discount.OfferConstants;
import com.hk.domain.builder.OfferActionBuilder;
import com.hk.domain.builder.OfferBuilder;
import com.hk.domain.builder.OfferTriggerBuilder;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferEmailDomain;
import com.hk.domain.offer.OfferTrigger;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.dao.offer.OfferDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.discount.CouponService;

@Component
public class OfferManager {

    @Autowired
    OfferDao              offerDao;

    @Autowired
    ProductService        productService;

    OfferInstanceDao      offerInstanceDaoProvider;

    @Autowired
    private CouponService couponService;

    @Autowired
    private RoleService   roleService;

    public boolean isOfferValidForUser(Offer offer, User user) {
      return isOfferValidForUserDomain(offer,  user) && isOfferValidForUserRole(offer,  user);      
    }

    public boolean isOfferValidForUserRole(Offer offer, User user) {
      if (!offer.getRoles().isEmpty()) {
        for (Role role : user.getRoles()) {
          if (offer.getRoles().contains(role)) {
            return true;
          }
        }
        return false;
      }
      return true;
    }

    public boolean isOfferValidForUserDomain(Offer offer, User user) {
      if (!offer.getOfferEmailDomains().isEmpty()) {
        for (OfferEmailDomain offerEmailDomain : offer.getOfferEmailDomains()) {
          if (user.getLogin().toLowerCase().endsWith(offerEmailDomain.getEmailDomain().toLowerCase())) {
            return true;
          }
        }
        return false;
      }
      return true;
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
            Role hkUserRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_USER).getRole();
            //roles.add(getRoleService().getRoleByName(RoleConstants.HK_USER));
            roles.add(hkUserRole);
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

        return offer;
    }

    @Transactional
    public Offer getAffiliateOffer() {
        Offer offer = getOfferDao().findByIdentifier(OfferConstants.affiliateCommissionOfferNew);

        if (offer == null) {

            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("Min purchase amount Rs 100").minAmount(100D).build();

            offerTrigger = (OfferTrigger) offerDao.save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("Additional 2% off on all Products").discountPercent(0.02).discountOnBasePriceOnly(false).validOnIntlShipping(
                    false).productGroup(getProductService().findProductGroupByName(OfferConstants.productGroupExcludingServices)).qty(1L).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("Additional 2% off on all Products, Affiliate program discount").startDate(new DateTime().toDate()).numberOfTimesAllowed(
                    1000L).carryOverAllowed(false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.affiliateCommissionOfferNew).offerTrigger(offerTrigger).offerAction(
                    offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            Role hkUserRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_USER).getRole();
            roles.add(hkUserRole);
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

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
            Role hkIHOUserRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_IHO_USER).getRole();
            roles.add(hkIHOUserRole);
            offer.setRoles(roles);

            offer = (Offer) getOfferDao().save(offer);
        }

        return offer;
    }

    @Transactional
    public Offer getOfferForReferralAndAffiliateProgram() {
        Offer offer = getOfferDao().findByIdentifier(OfferConstants.referralAffiliateOffer5percent);

        if (offer == null) {

            OfferTrigger offerTrigger = OfferTriggerBuilder.getInstance().description("Min purchase amount Rs 500").minAmount(500D).build();

            offerTrigger = (OfferTrigger) getOfferDao().save(offerTrigger);

            OfferAction offerAction = OfferActionBuilder.getInstance().description("5% Additional Off on all Products").discountAmount(250D).discountPercent(0.05).discountOnBasePriceOnly(
                    false).validOnIntlShipping(false).qty(1L).productGroup(getProductService().findProductGroupByName(OfferConstants.referralAffiliateProductGroup)).build();
            offerAction = (OfferAction) getOfferDao().save(offerAction);

            offer = OfferBuilder.getInstance().description("5% Additional Off on all Products, Min Purchase Rs 500/- Max Discount Rs 250/-").startDate(new DateTime().toDate()).numberOfTimesAllowed(
                    1L).carryOverAllowed(false).excludeTriggerProducts(false).offerIdentifier(OfferConstants.referralAffiliateOffer5percent).offerTrigger(offerTrigger).offerAction(
                    offerAction).build();

            Set<Role> roles = new HashSet<Role>(1);
            Role hkUserRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_USER).getRole();
            //roles.add(getRoleService().getRoleByName(RoleConstants.HK_USER));
            roles.add(hkUserRole);
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
            Role hkEmployeeRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_EMPLOYEE).getRole();
            roles.add(hkEmployeeRole);
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

}
