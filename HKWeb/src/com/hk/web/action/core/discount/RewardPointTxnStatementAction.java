package com.hk.web.action.core.discount;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HttpCache;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.user.User;
import com.hk.pact.service.order.RewardPointService;

@Component
@HttpCache(allow = false)
public class RewardPointTxnStatementAction extends BaseAction {

    @Autowired
    private RewardPointService rewardPointService;
    private User               user;
    private Double             redeemablePoint;

    public Resolution pre() {
        user = getPrincipalUser();
        redeemablePoint = rewardPointService.getTotalRedeemablePoints(user);
        if (isHybridRelease()) {
            return new ForwardResolution("/pages/rewardPointTxnStatementBeta.jsp");
        }

        return new ForwardResolution("/pages/rewardPointTxnStatement.jsp");
    }

    public Double getRedeemablePoint() {
        return redeemablePoint;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}