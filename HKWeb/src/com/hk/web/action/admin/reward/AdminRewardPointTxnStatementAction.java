package com.hk.web.action.admin.reward;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.pact.service.order.RewardPointService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_USERS }, authActionBean = AdminPermissionAction.class)
@Component
public class AdminRewardPointTxnStatementAction extends BaseAction {

    @Autowired
    private RewardPointService rewardPointService;

    @Validate(required = true)
    private User               user;

    private Double             redeemablePoint;

    public Resolution pre() {
        redeemablePoint = rewardPointService.getTotalRedeemablePoints(user);
        return new ForwardResolution("/pages/admin/rewardPointTxnStatement.jsp");
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
