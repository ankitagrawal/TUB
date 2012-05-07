package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.LocalizableError;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.User;
import com.hk.manager.UserManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.dao.user.UserDao;

@Secure(hasAnyRoles = { RoleConstants.HK_USER, RoleConstants.HK_UNVERIFIED }, disallowRememberMe = true)
@Component
public class MyAccountAction extends BaseAction {

    User              user;
    B2bUserDetails    b2bUserDetails;

    String            oldPassword;
    String            newPassword;
    String            confirmPassword;
    @Autowired
    UserDao           userDao;
    @Autowired
    B2bUserDetailsDao b2bUserDetailsDao;
    @Autowired
    UserManager       userManager;
    @Autowired
    RoleDao           roleDao;

    @DefaultHandler
    public Resolution pre() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(user);
        }

        return new ForwardResolution("/pages/userProfile.jsp");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Resolution save() {
        getPrincipal().setName(user.getName());
        userDao.save(user);
        if (user.getRoles().contains(roleDao.find(EnumRole.B2B_USER.getRoleName()))) {
            b2bUserDetailsDao.save(b2bUserDetails);
        }

        addRedirectAlertMessage(new SimpleMessage("User information has been updated"));
        return new RedirectResolution(MyAccountAction.class);
    }

    public Resolution changePassword() {
        user = getUserService().getUserById(getPrincipal().getId());

        if (!StringUtils.isBlank(oldPassword)) {
            if (StringUtils.equals(user.getPasswordChecksum(), BaseUtils.passwordEncrypt(oldPassword))) {
                if (!StringUtils.isBlank(newPassword) && !StringUtils.isBlank(confirmPassword)) {
                    if (StringUtils.equals(newPassword, confirmPassword)) {
                        user.setPassword(newPassword);
                        user.setPasswordChecksum(BaseUtils.passwordEncrypt(newPassword));
                    } else {
                        addValidationError("e1", new LocalizableError("/MyAccountAction.action.password.mismatch"));
                    }
                }
            } else
                addValidationError("e1", new LocalizableError("/MyAccountAction.action.oldpassword.incorrect"));
        }
        userDao.save(user);

        addRedirectAlertMessage(new SimpleMessage("Password has been updated successfully."));
        return new RedirectResolution(MyAccountAction.class);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public B2bUserDetails getB2bUserDetails() {
        return b2bUserDetails;
    }

    public void setB2bUserDetails(B2bUserDetails b2bUserDetails) {
        this.b2bUserDetails = b2bUserDetails;
    }
}