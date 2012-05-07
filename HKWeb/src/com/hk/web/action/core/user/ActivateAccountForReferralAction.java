package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.J2EESecurityManager;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.service.RoleService;
import com.hk.web.action.core.auth.LoginAction;

@Secure
@Component
public class ActivateAccountForReferralAction extends BaseAction {

    @Autowired
    private RoleService roleService;

    public Resolution pre() {
        User user = getPrincipalUser();
        Role verifiedRole = getRoleService().getRoleByName(RoleConstants.HK_USER);
        String redirectUrl = getContext().getRequest().getParameter(J2EESecurityManager.redirectAfterLoginParam);
        if (user != null && user.getRoles().contains(verifiedRole) && StringUtils.isNotBlank(redirectUrl)) {
            return new RedirectResolution(redirectUrl, false);
        }
        Role tempRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
        if (user != null && user.getRoles().contains(tempRole) && StringUtils.isNotBlank(redirectUrl)) {
            return new ForwardResolution(LoginAction.class);
        }
        return new ForwardResolution("/pages/activateAccountForReferral.jsp");
    }


    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}