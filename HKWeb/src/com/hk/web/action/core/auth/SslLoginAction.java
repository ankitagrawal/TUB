package com.hk.web.action.core.auth;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.manager.UserManager;
import com.hk.pact.dao.RoleDao;
import com.hk.web.action.HomeAction;
import com.hk.web.action.admin.AdminHomeAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

@Component
public class SslLoginAction extends BaseAction {

    @Validate(required = true)
    String email;

    @Validate(required = true)
    String password;

    private String redirectUrl;
    private boolean rememberMe;
    private String source;

    @SuppressWarnings("unused")
    @Session(key = HealthkartConstants.Session.userId)
    private String userId;

    public static final String SOURCE_CHECKOUT = "checkout";

    @Autowired
    UserManager userManager;
    @Autowired
    RoleDao roleDao;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/login.jsp");
    }

    public Resolution login() {
        UserLoginDto userLoginDto = null;
        try {
            userLoginDto = userManager.login(email, password, true);
        } catch (HealthkartLoginException e) {
            // Note: if the login fails, existing subject is still retained
            addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
            return getContext().getSourcePageResolution();
        }

        userId = userLoginDto.getLoggedUser().getId().toString();

        getContext().getRequest().getSession().setAttribute(HealthkartConstants.Session.orderCountSetBoolean, false); // this will force the orderCount var to be re-set for this session

        if (!StringUtils.isBlank(redirectUrl)) {
            return new RedirectResolution(redirectUrl, false);
        }

        if (userLoginDto.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.ADMIN))
                || userLoginDto.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.GOD))
                || userLoginDto.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.CATEGORY_MANAGER))
                || userLoginDto.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.OPS_MANAGER))
                || userLoginDto.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.WH_MANAGER))
                || userLoginDto.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.TICKETADMIN))
                || userLoginDto.getLoggedUser().getRoles().contains(getRoleService().getRoleByName(RoleConstants.CUSTOMER_SUPPORT)))
            return new RedirectResolution(AdminHomeAction.class);
        else
            return new RedirectResolution(HomeAction.class);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}