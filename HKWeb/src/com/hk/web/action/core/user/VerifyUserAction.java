package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.TempToken;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.OfferManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.service.RoleService;

@Component
public class VerifyUserAction extends BaseAction {

    @SuppressWarnings("unused")
    private static Logger      logger                  = LoggerFactory.getLogger(VerifyUserAction.class);
    @Autowired
    TempTokenDao               tempTokenDao;
    @Autowired
    OfferManager               offerManager;
    @Autowired
    UserManager                userManager;
    @Autowired
    OfferInstanceDao           offerInstanceDao;
    @Autowired
    CouponDao                  couponDao;
    @Autowired
    private RoleService        roleService;

    @Validate(required = true)
    String                     token;

    String                     outcome;
    boolean                    freePrints;
    boolean                    referrelDiscountApplied;

    private User               user;

    public static final String outcome_invalidLink     = "invalidLink";
    public static final String outcome_linkExpired     = "linkExpired";
    public static final String outcome_success         = "success";
    public static final String outcome_alreadyVerified = "alreadyVerified";

    public Resolution pre() {

        TempToken tempToken = tempTokenDao.findByToken(token);

        if (tempToken == null) {
            outcome = outcome_invalidLink;

            if (isHybridRelease()) {
                return new ForwardResolution("/pages/verifyUserBeta.jsp");
            }
            return new ForwardResolution("/pages/verifyUser.jsp");

        }

        if (!tempToken.isValid()) {
            tempTokenDao.expire(tempToken);
            outcome = outcome_linkExpired;
            if (isHybridRelease()) {
                return new ForwardResolution("/pages/verifyUserBeta.jsp");
            }
            return new ForwardResolution("/pages/verifyUser.jsp");

        }

        user = tempToken.getUser();
        Role unverifiedRole = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);
        Role deactivatedRole = getRoleService().getRoleByName(RoleConstants.HK_DEACTIVATED);
        if (user.getRoles().contains(unverifiedRole) || user.getRoles().contains(deactivatedRole)) {

            user.getRoles().remove(unverifiedRole);
            user.getRoles().remove(deactivatedRole);

            user.getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_USER));
            user = getUserService().save(user);

            // this creates a default email subscription row
            /*
             * EmailSubscription emailSubscription = userManager.getOrCreateEmailSubscription(user);
             * emailSubscription.setPromotional(true); emailSubscriptionDao.save(emailSubscription);
             */

            outcome = outcome_success;
            tempTokenDao.expire(tempToken);
            // } else if (user.getRoles().contains(unverifiedAffiliateRole)) {
            // user.getRoles().remove(unverifiedAffiliateRole);
            // user.getRoles().add(getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE));
            // user=userDao.save(user);
            // outcome=outcome_success;
            // tempTokenDao.expire(tempToken);
        } else {
            outcome = outcome_alreadyVerified;
        }

        //return new ForwardResolution("/pages/verifyUser.jsp");
        if (isHybridRelease()) {
            return new ForwardResolution("/pages/verifyUserBeta.jsp");
        }
        return new ForwardResolution("/pages/verifyUser.jsp");
    }

    public String getOutcome() {
        return outcome;
    }

    public boolean isFreePrints() {
        return freePrints;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isReferrelDiscountApplied() {
        return referrelDiscountApplied;
    }

    public User getUser() {
        return user;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}
