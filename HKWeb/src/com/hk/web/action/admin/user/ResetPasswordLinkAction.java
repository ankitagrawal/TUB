package com.hk.web.action.admin.user;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.web.action.core.user.ForgotPasswordAction;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 1 Feb, 2010 3:13:01 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_USER }, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 3, name = "Reset password link for user: {user.login}", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class ResetPasswordLinkAction extends BaseAction {
    @Autowired
    LinkManager    linkManager;
    @Autowired
    TempTokenDao   tempTokenDao;

    @Validate(required = true)
    private User   user;

    private String resetPasswordLink;

    public Resolution pre() {
        TempToken tempToken = tempTokenDao.createNew(user, ForgotPasswordAction.EXPIRY_DAYS);
        resetPasswordLink = linkManager.getResetPasswordLink(tempToken);
        return new ForwardResolution("/pages/admin/resetPasswordLink.jsp");
    }

    public String getResetPasswordLink() {
        return resetPasswordLink;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
