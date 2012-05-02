package com.hk.manager;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.EnumAffiliateTxnType;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.dao.CheckDetailsDao;
import com.hk.dao.affiliate.AffiliateCategoryDao;
import com.hk.dao.affiliate.AffiliateTxnDao;
import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.offer.Offer;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.filter.CartLineItemFilter;
import com.hk.service.AffilateService;
import com.hk.service.CouponService;
import com.hk.service.RoleService;
import com.hk.service.UserService;

@Component
public class AffiliateManager {


    @Autowired
    private UserService                    userService;
    @Autowired
    private RoleService                    roleService;
    @Autowired
    private CouponService                  couponService;
    @Autowired
    private AffilateService                affilateService;

    @Autowired
    private EmailManager                   emailManager;
    @Autowired
    private UserManager                    userManager;
    @Autowired
    private OfferManager                   offerManager;

    @Autowired
    private AffiliateTxnDao                affiliateTxnDao;
    @Autowired
    private CheckDetailsDao                checkDetailsDao;
    @Autowired
    private AffiliateCategoryDao affiliateCategoryCommissionDao;

    private String createCode(User user) {
        String code = user.getName().replace(" ", "");
        int codeLength = code.length();
        return codeLength > 10 ? code.substring(0, 10) + BaseUtils.getRandomStringTypable(5) : code + BaseUtils.getRandomStringTypable(15 - codeLength);
    }

    public Affiliate signup(String name, String email, String password, String websiteName) throws Exception {
        Affiliate affiliate = new Affiliate();
        try {
            affiliate.setUser(getUserManager().signup(email, name, password, null, RoleConstants.HK_AFFILIATE_UNVERIFIED));
            affiliate.getUser().getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED));
        } catch (HealthkartSignupException e) {
            throw new HealthkartSignupException("affiliate by this email id already exists");
        }
        String code = createCode(affiliate.getUser());
        while (getCouponService().findByCode(code) != null) {
            code = createCode(affiliate.getUser());
        }
        affiliate.setCode(code);
        affiliate.setWebsiteName(websiteName);
        getAffilateService().save(affiliate);
        Offer offer = getOfferManager().getAffiliateOffer();
        getCouponService().createCoupon(affiliate.getCode(), null, null, 0L, offer, affiliate.getUser(), false);

        getEmailManager().affiliateSignupEmail(affiliate.getUser().getEmail(), affiliate.getUser().getName());
        return affiliate;
    }

    public Affiliate login(String email, String password, boolean rememberMe) throws Exception {
        User user;
        try {
            user = (getUserManager().login(email, password, rememberMe).getLoggedUser());
        } catch (HealthkartLoginException e) {
            throw new HealthkartLoginException(e);
        }
        return getAffilateService().getAffilateByUser(user);
    }

    public Affiliate userToAffiliate(User user, String websiteName) throws HealthkartSignupException {
        if (getAffilateService().getAffiliateByUserId(user.getId()) != null) {
            throw new HealthkartSignupException("User already exists by this id/email");
        }
        Affiliate affiliate = new Affiliate();
        Set<Role> roles = user.getRoles();
        roles.add(getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED));
        user.setRoles(roles);
        getUserService().save(user);
        affiliate.setUser(user);
        affiliate.setWebsiteName(websiteName);
        String code = createCode(affiliate.getUser());
        while (getCouponService().findByCode(code) != null) {
            code = createCode(affiliate.getUser());
        }
        affiliate.setCode(code);
        getAffilateService().save(affiliate);
        Offer offer = getOfferManager().getAffiliateOffer();
        getCouponService().createCoupon(affiliate.getCode(), null, null, 0L, offer, affiliate.getUser(), false);
        getEmailManager().affiliateSignupEmail(user.getEmail(), user.getName());
        return affiliate;
    }

    // NEW FUNCTION ADDED BECAUSE THE DATABASE ENTRY CHANGED WITH NEGATIVE VALUES WHEN AMOUNT IS PAID
    // public Double getAmountInAccount(Affiliate affiliate) {
    // Double affiliateAccountAmount = 0D;
    // List<AffiliateTxn> affiliateTxnList = getAffiliateTxnDao().getTxnListByAffiliate(affiliate);
    // for (AffiliateTxn affiliateTxn : affiliateTxnList) {
    // if (affiliateTxn.getAffiliateTxnType() ==
    // affiliateTxnTypeDaoProvider.get().find(EnumAffiliateTxnType.ADD.getId())) {
    // affiliateAccountAmount += affiliateTxn.getAmount();
    // } else if (affiliateTxn.getAffiliateTxnType() ==
    // affiliateTxnTypeDaoProvider.get().find(EnumAffiliateTxnType.SENT.getId())) {
    // // to handle a case if account balance is negative and we still pay him for some reason
    // if (affiliateAccountAmount >= 0) {
    // affiliateAccountAmount -= affiliateTxn.getAmount();
    // } else {
    // affiliateAccountAmount += affiliateTxn.getAmount();
    // }
    // }
    // }
    // return affiliateAccountAmount;
    // }

    public Double getAmountInAccount(Affiliate affiliate) {
        Double affiliateAccountAmount = 0D;
        affiliateAccountAmount = getAffiliateTxnDao().getAmountInAccount(affiliate);
        return affiliateAccountAmount;
    }

    public void addAmountInAccountforFirstTransaction(User affiliateUser, Order order) {
        Affiliate affiliate = getAffilateService().getAffilateByUser(affiliateUser);
        Double affiliateSumTotal = 0D;
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem cartLineItem : productCartLineItems) {
            AffiliateCategory affiliateCategory = cartLineItem.getProductVariant().getAffiliateCategory();
            if (affiliateCategory != null) {
                AffiliateCategoryCommission affiliateCategoryCommission = getAffiliateCategoryCommissionDao().getCommissionAffiliateWise(affiliate, affiliateCategory);
                if (affiliateCategoryCommission != null) {
                    affiliateSumTotal += cartLineItem.getProductVariant().getHkPrice(null) * cartLineItem.getQty() * affiliateCategoryCommission.getCommissionFirstTime() * 0.01;
                }
            }
        }
        AffiliateTxnType affiliateTxnType = getAffilateService().getAffiliateTxnType(EnumAffiliateTxnType.ADD.getId());
        getAffiliateTxnDao().saveTxn(affiliate, affiliateSumTotal, affiliateTxnType, order);
    }

    public void addAmountInAccountforLatterTransaction(User affiliateUser, Order order) {
        Affiliate affiliate = getAffilateService().getAffilateByUser(affiliateUser);
        Double affiliateSumTotal = 0D;
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem cartLineItem : productCartLineItems) {
            AffiliateCategory affiliateCategory = cartLineItem.getProductVariant().getAffiliateCategory();
            if (affiliateCategory != null) {
                AffiliateCategoryCommission affiliateCategoryCommission = getAffiliateCategoryCommissionDao().getCommissionAffiliateWise(affiliate, affiliateCategory);
                if (affiliateCategoryCommission != null) {
                    affiliateSumTotal += cartLineItem.getProductVariant().getHkPrice() * cartLineItem.getQty() * affiliateCategoryCommission.getCommissionLatterTime() * 0.01;
                }
            }
        }
        AffiliateTxnType affiliateTxnType = getAffilateService().getAffiliateTxnType(EnumAffiliateTxnType.ADD.getId());
        getAffiliateTxnDao().saveTxn(affiliate, affiliateSumTotal, affiliateTxnType, order);
    }

    public void paidToAffiiliate(Affiliate affiliate, Double amountPaid, CheckDetails checkDetails) {
        AffiliateTxn affiliateTxn = new AffiliateTxn();
        AffiliateTxnType affiliateTxnType = getAffilateService().getAffiliateTxnType(EnumAffiliateTxnType.SENT.getId());
        affiliateTxn = getAffiliateTxnDao().saveTxn(affiliate, amountPaid, affiliateTxnType, null);
        checkDetails.setAffiliateTxn(affiliateTxn);
        checkDetails.setAffiliate(affiliate);
        getCheckDetailsDao().save(checkDetails);
    }

    public void verifyAffiliate(User user, String customMessage) {
        Role unverifiedAffiliateRole = getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED);
        if (user.getRoles().contains(unverifiedAffiliateRole)) {
            user.getRoles().remove(unverifiedAffiliateRole);
            Role hkUnVerifiedRole = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);
            if (user.getRoles().contains(hkUnVerifiedRole)) {
                user.getRoles().remove(hkUnVerifiedRole);
            }
            user.getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_USER));
            user.getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE));
            user = getUserService().save(user);
            Affiliate affiliate = getAffilateService().getAffilateByUser(user);
            getAffiliateCategoryCommissionDao().insertCategoryCommissionsAffiliateWise(affiliate);
            getAffilateService().save(affiliate);
            getEmailManager().affiliateVerfiedEmail(user.getEmail(), user.getName(), affiliate.getCode(), customMessage);
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public AffiliateTxnDao getAffiliateTxnDao() {
        return affiliateTxnDao;
    }

    public void setAffiliateTxnDao(AffiliateTxnDao affiliateTxnDao) {
        this.affiliateTxnDao = affiliateTxnDao;
    }

    public CouponService getCouponService() {
        return couponService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    public OfferManager getOfferManager() {
        return offerManager;
    }

    public void setOfferManager(OfferManager offerManager) {
        this.offerManager = offerManager;
    }

    public CheckDetailsDao getCheckDetailsDao() {
        return checkDetailsDao;
    }

    public void setCheckDetailsDao(CheckDetailsDao checkDetailsDao) {
        this.checkDetailsDao = checkDetailsDao;
    }

    public AffilateService getAffilateService() {
        return affilateService;
    }

    public void setAffilateService(AffilateService affilateService) {
        this.affilateService = affilateService;
    }

    public AffiliateCategoryDao getAffiliateCategoryCommissionDao() {
        return affiliateCategoryCommissionDao;
    }

    public void setAffiliateCategoryCommissionDao(AffiliateCategoryDao affiliateCategoryCommissionDao) {
        this.affiliateCategoryCommissionDao = affiliateCategoryCommissionDao;
    }

}
