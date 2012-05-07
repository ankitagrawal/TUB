package com.hk.web.action.core.discount;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.user.User;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.service.UserService;

@Component
public class RewardPointTxnStatementAction extends BaseAction {

    @Autowired
    ReferrerProgramManager referrerProgramManager;

    @Autowired
    private UserService    userService;
    private User           user;

    private Double         redeemablePoint;

    public Resolution pre() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        System.out.println("test11111");
        redeemablePoint = referrerProgramManager.getTotalRedeemablePoints(user);
        System.out.println("test");
        return new ForwardResolution("/pages/rewardPointTxnStatement.jsp");
    }

    public Double getRedeemablePoint() {
        return redeemablePoint;
    }

    public User getUser() {
        return user;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}