package com.hk.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.security.HKAuthFailureHandler;
import com.hk.security.HKAuthService;
import com.hk.security.HKAuthSuccessHandler;
import com.hk.security.HKAuthentication;
import com.hk.security.HKUsernamePasswordAuthenticationToken;
import com.hk.security.exception.HKAuthenticationException;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;

@SuppressWarnings("serial")
public class HKSingleLoginServlet extends HttpServlet {

    private static Logger        logger            = LoggerFactory.getLogger(HKSingleLoginServlet.class);

    public static final String   JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    public static final String   HK_USERNAME_KEY   = "userName";
    public static final String   HK_PASSWORD_KEY   = "password";
    public static final String   HK_APP_ID_KEY     = "appId";

    private String               userNameParameter = HK_USERNAME_KEY;
    private String               passwordParameter = HK_PASSWORD_KEY;
    private String               appIdParameter    = HK_APP_ID_KEY;

    private HKAuthService        hkAuthService;

    private HKAuthFailureHandler failureHandler;
    private HKAuthSuccessHandler successHandler;

    // TODO: change this to post
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
         * if ( !req.getMethod().equals("POST")) { throw new AuthenticationServiceException("Authentication method not
         * supported: " + request.getMethod()); }
         */

        String appId = obtainAppId(req);
        String login = obtainUsername(req);
        String password = obtainPassword(req);

        if (StringUtils.isBlank(appId)) {
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

        HKAuthentication authResult;

        try {
            authResult = attemptAuthentication(req, resp);
            if (authResult == null) {
                // return immediately as subclass has indicated that it hasn't completed authentication
                return;
            }

        } catch (HKAuthenticationException failed) {
            unsuccessfulAuthentication(req, resp, failed);
            return;
        }

        successfulAuthentication(req, resp, authResult);

    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, HKAuthenticationException failed) throws IOException, ServletException {
        getFailureHandler().handleAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, HKAuthentication authResult) throws IOException, ServletException {
        getSuccessHandler().handleAuthenticationSuccess(request, response, authResult);
    }

    protected HKAuthentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws HKAuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String appId = obtainAppId(request);

        username = username.trim();

        HKUsernamePasswordAuthenticationToken authRequest = new HKUsernamePasswordAuthenticationToken(username, password, appId);

        return getHkAuthService().authenticate(authRequest);
    }

    private String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(userNameParameter);
    }

    private String obtainAppId(HttpServletRequest request) {
        return request.getParameter(appIdParameter);
    }

    private void writeExceptionResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        writeJSONResponse(req, resp, jsonResponse);

    }

    private void writeJSONResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setContentType(JSON_CONTENT_TYPE);
        String callback = req.getParameter("callback");
        if (callback != null) {
            jsonResponse = callback + "(" + jsonResponse + ")";
        }
        writeResponse(req, resp, jsonResponse);
    }

    private void writeResponse(HttpServletRequest req, HttpServletResponse resp, String response) {
        try {
            resp.getWriter().write(response);
        } catch (IOException e) {
            logger.error("IO Exception occured in writing response :" + response, e);
        }
    }

    public HKAuthService getHkAuthService() {
        if (hkAuthService == null) {
            hkAuthService = ServiceLocatorFactory.getService(HKAuthService.class);
        }
        return hkAuthService;
    }

    public HKAuthFailureHandler getFailureHandler() {
        if (failureHandler == null) {
            failureHandler = ServiceLocatorFactory.getService(HKAuthFailureHandler.class);
        }
        return failureHandler;
    }

    public HKAuthSuccessHandler getSuccessHandler() {
        if (successHandler == null) {
            successHandler = ServiceLocatorFactory.getService(HKAuthSuccessHandler.class);
        }
        return successHandler;
    }

}
