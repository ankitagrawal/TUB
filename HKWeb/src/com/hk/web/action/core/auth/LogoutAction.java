package com.hk.web.action.core.auth;

import javax.servlet.http.Cookie;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.lang.StringUtils;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.pact.service.UserSessionService;
import com.hk.service.ServiceLocatorFactory;

/**
 * User: kani Time: 7 Oct, 2009 2:06:17 PM
 */
public class LogoutAction extends BaseAction {
    private String             redirectUrl;

    private UserSessionService userSessionService;

    public Resolution pre() {

        getUserSessionService().onLogoutUser();
        getSubject().logout();

        // removing vanilla forum cookies if any
        Cookie[] cookies = getContext().getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().toLowerCase().contains("vanilla")) {
                    cookie.setMaxAge(0);
                    getContext().getResponse().addCookie(cookie);
                }
            }
        }

        if (!StringUtils.isBlank(redirectUrl)) {
            return new RedirectResolution(redirectUrl, false);
        }

        return new RedirectResolution(LoginAction.class);
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public UserSessionService getUserSessionService() {
        if (userSessionService == null) {
            userSessionService = ServiceLocatorFactory.getService(UserSessionService.class);
        }
        return userSessionService;
    }

}
