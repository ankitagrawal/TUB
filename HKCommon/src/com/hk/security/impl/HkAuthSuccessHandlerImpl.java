package com.hk.security.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.util.BaseUtils;
import com.hk.cache.HkApiUserCache;
import com.hk.domain.api.HkApiUser;
import com.hk.security.HkAuthService;
import com.hk.security.HkAuthSuccessHandler;
import com.hk.security.HkAuthentication;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HkAuthSuccessHandlerImpl implements HkAuthSuccessHandler {

    private static final String REDIRECT_URL_PARAM = "redirectUrl";
    @Autowired
    private HkAuthService       hkAuthService;

    @Override
    public void handleAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, HkAuthentication authResult) throws IOException {
        HkApiUser hkApiUser = HkApiUserCache.getInstance().getHkApiUser((String) authResult.getApiKey());

        String targetUrl = determineTargetUrl(request, response, hkApiUser.getLoginSuccessRedirectUrl());

        String redirectUrl = calculateRedirectUrl(request.getContextPath(), targetUrl);

        String userName = (String) authResult.getPrincipal();
        String password = (String) authResult.getCredentials();
        String passwordCheckSum = BaseUtils.passwordEncrypt(password);
        String apiKey = (String) authResult.getApiKey();

        redirectUrl = redirectUrl.concat("?").concat(getHkAuthService().generateAuthToken(userName, passwordCheckSum, apiKey));
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        response.sendRedirect(redirectUrl);

    }

    private String calculateRedirectUrl(String contextPath, String url) {
        boolean contextRelative = false;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (contextRelative) {
                return url;
            } else {
                return contextPath + url;
            }
        }

        // Full URL, including http(s)://

        if (!contextRelative) {
            return url;
        }

        // Calculate the relative URL from the fully qualifed URL, minus the protocol and base context.
        url = url.substring(url.indexOf("://") + 3); // strip off protocol
        url = url.substring(url.indexOf(contextPath) + contextPath.length());

        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }

        return url;
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, String loginSuccessRedirectUrl) {

        String redirectUrlFromReq = request.getParameter(REDIRECT_URL_PARAM);
        if (StringUtils.isNotBlank(redirectUrlFromReq)) {
            return redirectUrlFromReq;
        } else {
            return loginSuccessRedirectUrl;
        }

    }

    public HkAuthService getHkAuthService() {
        return hkAuthService;
    }

}
