package com.hk.manager;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.util.BaseUtils;
import com.hk.cache.RoleCache;
import com.hk.constants.affiliate.AffiliateConstants;
import com.hk.constants.affiliate.EnumAffiliateStatus;
import com.hk.constants.affiliate.EnumAffiliateTxnType;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.impl.dao.affiliate.AffiliateCategoryDaoImpl;
import com.hk.impl.dao.affiliate.AffiliateTxnDaoImpl;
import com.hk.pact.dao.CheckDetailsDao;
import com.hk.pact.dao.affiliate.AffiliateCategoryDao;
import com.hk.pact.dao.affiliate.AffiliateTxnDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.discount.CouponService;

@Component
public class AffiliateManager {

    @Autowired
    private UserService          userService;
    /*
     * @Autowired private RoleService roleService;
     */
    @Autowired
    private CouponService        couponService;
    @Autowired
    private AffilateService      affilateService;

    @Autowired
    private EmailManager         emailManager;
    @Autowired
    private UserManager          userManager;
    @Autowired
    private OfferManager         offerManager;

    @Autowired
    private AffiliateTxnDao      affiliateTxnDao;
    @Autowired
    private CheckDetailsDao      checkDetailsDao;
    @Autowired
    private AffiliateCategoryDao affiliateCategoryCommissionDao;

    private String createCode(User user) {
        String code = user.getName().replace(" ", "");
        int codeLength = code.length();
        return codeLength > 10 ? code.substring(0, 10) + BaseUtils.getRandomStringTypable(5) : code + BaseUtils.getRandomStringTypable(15 - codeLength);
    }

    public Affiliate signup(String name, String email, String password, String websiteName, Long affiliateType, Long affiliateMode) throws Exception {
        Affiliate affiliate = new Affiliate();
        try {
            affiliate.setUser(getUserManager().signup(email, name, password, null, RoleConstants.HK_AFFILIATE_UNVERIFIED));
            // affiliate.getUser().getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED));
            affiliate.getUser().getRoles().add(RoleCache.getInstance().getRoleByName(RoleConstants.HK_UNVERIFIED).getRole());
        } catch (HealthkartSignupException e) {
            throw new HealthkartSignupException("affiliate by this email id already exists");
        }
        String code = createCode(affiliate.getUser());
        while (getCouponService().findByCode(code) != null) {
            code = createCode(affiliate.getUser());
        }
        affiliate.setCode(code);
        affiliate.setAffiliateMode(affiliateMode);
        affiliate.setAffiliateType(affiliateType);
        affiliate.setValidDays(AffiliateConstants.affiliateExpiryDays);
        affiliate.setWeeklyCouponLimit(AffiliateConstants.weeklyCouponLimit);
        affiliate.setAffiliateStatus(EnumAffiliateStatus.Unverified.asAffiliateStatus());
        affiliate.setWebsiteName(websiteName);
        Offer offer = getOfferManager().getOfferForReferralAndAffiliateProgram();
        affiliate.setOffer(offer);
        getAffilateService().save(affiliate);
        // getCouponService().createCoupon(affiliate.getCode(), null, null, 0L, offer, affiliate.getUser(), false,
        // EnumCouponType.AFFILIATE.asCouponType());

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

    public Affiliate userToAffiliate(User user, String websiteName, Long affiliateMode, Long affiliateType) throws HealthkartSignupException {
        if (getAffilateService().getAffiliateByUserId(user.getId()) != null) {
            throw new HealthkartSignupException("User already exists by this id/email");
        }
        Affiliate affiliate = new Affiliate();
        Set<Role> roles = user.getRoles();
        // roles.add(getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED));
        roles.add(RoleCache.getInstance().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED).getRole());
        user.setRoles(roles);
        getUserService().save(user);
        affiliate.setUser(user);
        affiliate.setWebsiteName(websiteName);
        affiliate.setAffiliateMode(affiliateMode);
        affiliate.setAffiliateType(affiliateType);
        affiliate.setValidDays(AffiliateConstants.affiliateExpiryDays);
        affiliate.setWeeklyCouponLimit(AffiliateConstants.weeklyCouponLimit);
        affiliate.setAffiliateStatus(EnumAffiliateStatus.Unverified.asAffiliateStatus());
        String code = createCode(affiliate.getUser());
        while (getCouponService().findByCode(code) != null) {
            code = createCode(affiliate.getUser());
        }
        affiliate.setCode(code);
        Offer offer = getOfferManager().getOfferForReferralAndAffiliateProgram();
        affiliate.setOffer(offer);
        getAffilateService().save(affiliate);
        // getCouponService().createCoupon(affiliate.getCode(), null, null, 0L, offer, affiliate.getUser(), false,
        // EnumCouponType.AFFILIATE.asCouponType());
        getEmailManager().affiliateSignupEmail(user.getEmail(), user.getName());
        return affiliate;
    }

    public Double getAmountInAccount(Affiliate affiliate, Date startDate, Date endDate) {
        Double affiliateAccountAmount = 0D;
        affiliateAccountAmount = getAffiliateTxnDao().getAmountInAccount(affiliate, startDate, endDate);
        return affiliateAccountAmount;
    }

    public Double getPayableAmount(Affiliate affiliate) {
        return getAffiliateTxnDao().getPayableAmount(affiliate);
    }

    public void paidToAffiiliate(Affiliate affiliate, Double amountPaid, CheckDetails checkDetails) {
        AffiliateTxnType affiliateTxnType = getAffilateService().getAffiliateTxnType(EnumAffiliateTxnType.SENT.getId());
	      AffiliateTxn affiliateTxn = getAffiliateTxnDao().saveTxn(affiliate, amountPaid, affiliateTxnType, null, checkDetails.getIssueDate());
	      getAffiliateTxnDao().markDueAffiliateTxnAsPaid(affiliate, checkDetails.getIssueDate());
        checkDetails.setAffiliateTxn(affiliateTxn);
        checkDetails.setAffiliate(affiliate);
        getCheckDetailsDao().save(checkDetails);
    }

    public void verifyAffiliate(User user, String customMessage) {
        // Role unverifiedAffiliateRole = getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED);
        Role unverifiedAffiliateRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_AFFILIATE_UNVERIFIED).getRole();
        if (user.getRoles().contains(unverifiedAffiliateRole)) {
            user.getRoles().remove(unverifiedAffiliateRole);
            // Role hkUnVerifiedRole = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);

            Role hkUnVerifiedRole = RoleCache.getInstance().getRoleByName(RoleConstants.HK_UNVERIFIED).getRole();
            if (user.getRoles().contains(hkUnVerifiedRole)) {
                user.getRoles().remove(hkUnVerifiedRole);
            }
            /*
             * user.getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_USER));
             * user.getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE));
             */

            user.getRoles().add(RoleCache.getInstance().getRoleByName(RoleConstants.HK_USER).getRole());
            user.getRoles().add(RoleCache.getInstance().getRoleByName(RoleConstants.HK_AFFILIATE).getRole());

            user = getUserService().save(user);
            Affiliate affiliate = getAffilateService().getAffilateByUser(user);
            affiliate.setAffiliateStatus(EnumAffiliateStatus.Verified.asAffiliateStatus());
            getAffiliateCategoryCommissionDao().insertCategoryCommissionsAffiliateWise(affiliate);
            getAffilateService().save(affiliate);
            getEmailManager().affiliateVerfiedEmail(user.getEmail(), user.getName(), affiliate.getCode(), customMessage);
        }
    }

    public void rejectAffiliate(User user) {
        Affiliate affiliate = getAffilateService().getAffilateByUser(user);
        affiliate.setAffiliateStatus(EnumAffiliateStatus.Rejected.asAffiliateStatus());
        getAffilateService().save(affiliate);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /*
     * public RoleService getRoleService() { return roleService; } public void setRoleService(RoleService roleService) {
     * this.roleService = roleService; }
     */

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

    public void setAffiliateTxnDao(AffiliateTxnDaoImpl affiliateTxnDao) {
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

    public void setAffiliateCategoryCommissionDao(AffiliateCategoryDaoImpl affiliateCategoryCommissionDao) {
        this.affiliateCategoryCommissionDao = affiliateCategoryCommissionDao;
    }

}
