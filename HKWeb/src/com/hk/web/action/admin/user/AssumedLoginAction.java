package com.hk.web.action.admin.user;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.web.action.HomeAction;
import com.hk.web.action.core.b2b.B2BCartAction;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SUPER_LOGIN }, authActionBean = AdminPermissionAction.class, disallowRememberMe = true)
@Component
public class AssumedLoginAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(AssumedLoginAction.class);

    @Validate(required = true, encrypted = true)
    User                  user;

    @Autowired
    private UserService   userService;
    @Autowired
    private RoleService   roleService;
    @Autowired
    private EmailManager  emailManager;

    public Resolution login() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
            if (!loggedOnUser.getRoles().contains(getRoleService().getRoleByName(EnumRole.GOD.getRoleName()))) {// Logged on user is Not
                // a God
                // and Assumed login is an employee
                if (user.getRoles().contains(getRoleService().getRoleByName(EnumRole.GOD.getRoleName())) || user.getRoles().contains(getRoleService().getRoleByName(EnumRole.ADMIN.getRoleName()))
                        || user.getRoles().contains(getRoleService().getRoleByName(EnumRole.OPS_MANAGER.getRoleName()))
                        || user.getRoles().contains(getRoleService().getRoleByName(EnumRole.CATEGORY_MANAGER.getRoleName()))
                        || user.getRoles().contains(getRoleService().getRoleByName(EnumRole.CUSTOMER_SUPPORT.getRoleName()))
                        || user.getRoles().contains(getRoleService().getRoleByName(EnumRole.HK_EMPLOYEE.getRoleName()))) {

                    emailManager.sendProtocolViolationEmail(loggedOnUser, user);
                    logger.info(loggedOnUser.getLogin() + " is trying to be smart and superlogin for " + user.getLogin());
                    // addRedirectAlertMessage(new SimpleError("You trying to be smart. An Email is fired to Admins
                    // regarding this protocol violation."));
                    addRedirectAlertMessage(new SimpleError("Unhandled Exception."));
                    return new RedirectResolution(SearchUserAction.class);
                }
            }
        }
        getPrincipal().setAssumedIdentity(user);
        if(user.getRoles().contains(getRoleService().getRoleByName(EnumRole.B2B_USER.getRoleName())))
        {
        	 return new RedirectResolution(B2BCartAction.class);
        }
        return new RedirectResolution(HomeAction.class);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

}
