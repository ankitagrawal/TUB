package com.hk.web.action.admin.user;

import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Role;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.error.AdminPermissionAction;

import java.util.HashSet;
import java.util.Set;

/**
 * User: rahul
 * Time: 22 Jan, 2010 12:44:34 PM
 */
@Secure(hasAnyPermissions = PermissionConstants.UPDATE_USER, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 2, name = "Edit User: {user.login}", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class EditUserAction extends BaseAction {
    @Autowired
    UserDao userDao;

    @ValidateNestedProperties({
            @Validate(field = "name", required = true, on = "save"),
            @Validate(field = "login", required = true, on = "save")
    })
    private User user;

    @DefaultHandler
    public Resolution edit() {
        return new ForwardResolution("/pages/admin/editUser.jsp");
    }

    @ValidationMethod(on = "save")
    public void validate() {
        User userByLogin = userDao.findByLogin(user.getLogin());
        if (userByLogin != null && !userByLogin.getId().equals(user.getId())) {
            addValidationError("userLogin", new LocalizableError("/EditUser.action.user.login.not.unique"));
        }

    }

    public Resolution save() {
        getUserService().save(user);
        addRedirectAlertMessage(new LocalizableMessage("/EditUser.action.user.saved.successfully", user.getLogin()));
        return new RedirectResolution(SearchUserAction.class, "search");
    }
    @Secure(hasAnyPermissions = RoleConstants.GOD, authActionBean = AdminPermissionAction.class)
    public Resolution activateHKRocks() {
        user.getRoles().add(EnumRole.HK_EMPLOYEE.toRole());
        getUserService().save(user);
        return new RedirectResolution(SearchUserAction.class, "search");
    }

    @Secure(hasAnyPermissions = RoleConstants.B2B_ROLE, authActionBean = AdminPermissionAction.class)
    public Resolution activateB2bUser() {
        user.getRoles().add(EnumRole.B2B_USER.toRole());
            getUserService().save(user);
            return new RedirectResolution(SearchUserAction.class, "search");
        }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
