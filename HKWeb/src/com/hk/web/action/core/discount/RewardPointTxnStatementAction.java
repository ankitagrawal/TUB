package com.hk.web.action.core.discount;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.user.User;
import com.hk.manager.ReferrerProgramManager;

@Component
public class RewardPointTxnStatementAction extends BaseAction {

    @Autowired
    ReferrerProgramManager referrerProgramManager;
    private User user;
    private Double redeemablePoint;

    public Resolution pre() {
        user = getPrincipalUser();
        redeemablePoint = referrerProgramManager.getTotalRedeemablePoints(user);
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