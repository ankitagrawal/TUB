package com.hk.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.hk.security.HkAuthFailureHandler;
import com.hk.security.HkAuthService;
import com.hk.security.HkAuthSuccessHandler;
import com.hk.security.HkAuthentication;
import com.hk.security.HkUsernamePasswordAuthenticationToken;
import com.hk.security.exception.HkAuthenticationException;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class HKSingleLoginServlet extends HkAPIBaseServlet {

    public static final String   HK_USERNAME_KEY   = "userName";
    public static final String   HK_PASSWORD_KEY   = "password";
    public static final String   HK_API_KEY        = "apiKey";

    private String               userNameParameter = HK_USERNAME_KEY;
    private String               passwordParameter = HK_PASSWORD_KEY;
    private String               apiKeyParameter   = HK_API_KEY;

    private HkAuthService        hkAuthService;

    private HkAuthFailureHandler failureHandler;
    private HkAuthSuccessHandler successHandler;

    // TODO: change this to post
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
         * if ( !req.getMethod().equals("POST")) { throw new AuthenticationServiceException("Authentication method not
         * supported: " + request.getMethod()); }
         */

        String apiKey = obtainApiKey(req);
        String login = obtainUsername(req);
        String password = obtainPassword(req);

        if (StringUtils.isBlank(apiKey)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_APP_ID_PASSED").build());
            return;
        }

        if (StringUtils.isBlank(login)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_LOGIN_PASSED").build());
            return;
        }

        if (StringUtils.isBlank(password)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_PWD_PASSED").build());
            return;
        }

        HkAuthentication authResult;

        try {
            authResult = attemptAuthentication(req, resp);
            if (authResult == null) {
                // return immediately as subclass has indicated that it hasn't completed authentication
                return;
            }

        } catch (HkAuthenticationException failed) {
            unsuccessfulAuthentication(req, resp, failed);
            return;
        }

        successfulAuthentication(req, resp, authResult);

    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, HkAuthenticationException failed) throws IOException, ServletException {
        getFailureHandler().handleAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, HkAuthentication authResult) throws IOException, ServletException {
        getSuccessHandler().handleAuthenticationSuccess(request, response, authResult);
    }

    protected HkAuthentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws HkAuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String apiKey = obtainApiKey(request);

        username = username.trim();

        HkUsernamePasswordAuthenticationToken authRequest = new HkUsernamePasswordAuthenticationToken(username, password, apiKey);

        return getHkAuthService().authenticate(authRequest);
    }

    private String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(userNameParameter);
    }

    private String obtainApiKey(HttpServletRequest request) {
        return request.getParameter(apiKeyParameter);
    }

    public HkAuthService getHkAuthService() {
        if (hkAuthService == null) {
            hkAuthService = ServiceLocatorFactory.getService(HkAuthService.class);
        }
        return hkAuthService;
    }

    public HkAuthFailureHandler getFailureHandler() {
        if (failureHandler == null) {
            failureHandler = ServiceLocatorFactory.getService(HkAuthFailureHandler.class);
        }
        return failureHandler;
    }

    public HkAuthSuccessHandler getSuccessHandler() {
        if (successHandler == null) {
            successHandler = ServiceLocatorFactory.getService(HkAuthSuccessHandler.class);
        }
        return successHandler;
    }

}
