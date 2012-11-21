package com.hk.web.action.core.referral;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.user.User;
import com.hk.manager.ReferrerProgramManager;
import com.hk.manager.UserManager;
import com.hk.pact.service.order.RewardPointService;

@Secure
@Component
public class ReferralManagerAction extends BaseAction {

    @Autowired
    ReferrerProgramManager     referrerProgramManager;
    @Autowired
    UserManager                userManager;
    @Autowired
    RewardPointService rewardPointService;

    private Coupon             coupon;
    private User               user;
    private Double             rewardPointBalance;

    public Resolution pre() {
        user = getUserService().getUserById(getPrincipal().getId());
        if (user != null && userManager.getProcessedOrdersCount(user) > 0) {
            coupon = referrerProgramManager.getOrCreateRefferrerCoupon(user);// making it NOT available for non-order
            // user
        }
        rewardPointBalance = rewardPointService.getTotalRedeemablePoints(user);
        return new ForwardResolution("/pages/referralManager.jsp");
    }

    public User getUser() {
        return user;
    }

    public Double getRewardPointBalance() {
        return rewardPointBalance;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
