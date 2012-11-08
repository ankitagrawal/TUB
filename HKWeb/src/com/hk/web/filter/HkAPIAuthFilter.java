package com.hk.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.api.HkAPI;
import com.hk.api.user.HkAPIUser;
import com.hk.security.HkAuthService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;
import com.hk.web.locale.LocaleContextHolder;

/**
 * @author vaibhav.adlakha
 */
public class HkAPIAuthFilter implements Filter {

    private static Logger      logger            = LoggerFactory.getLogger(HkAPIAuthFilter.class);

    public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    private HkAuthService      hkAuthService;

    protected void writeExceptionResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writeJSONResponse(req, resp, jsonResponse);
    }

    protected void writeJSONResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setContentType(JSON_CONTENT_TYPE);
        String callback = req.getParameter("callback");
        if (callback != null) {
            jsonResponse = callback + "(" + jsonResponse + ")";
        }
        writeResponse(req, resp, jsonResponse);
    }

    protected void writeResponse(HttpServletRequest req, HttpServletResponse resp, String response) {
        try {
            resp.getWriter().write(response);
        } catch (IOException e) {
            logger.error("IO Exception occured in writing response :" + response, e);
        }
    }

    protected void writeSuccessResponse(HttpServletRequest req, HttpServletResponse resp, String response) {
        writeJSONResponse(req, resp, response);
    }

    protected void writeInvalidParametersException(HttpServletRequest req, HttpServletResponse resp) {
        writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_PARAMETER").build());
    }

    protected void writeMissingParametersException(HttpServletRequest req, HttpServletResponse resp, String msg) {
        writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("msg", msg).addField("code", "MISSING_PARAMETER").build());
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) arg0;
        HttpServletResponse resp = (HttpServletResponse) arg1;

        String authToken = req.getParameter("authToken");
        String apiVersion = req.getParameter("apiVersion");
        String apiKey = req.getParameter("apiKey");

        // check for a not null authToken
        if (StringUtils.isEmpty(authToken)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_AUTH_TOKEN_PASSED").build());
            return;
        }

        // check for a not null apiKey
        if (StringUtils.isEmpty(apiKey)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_API_KEY_PASSED").build());
            return;
        }

        if (!HkAPIUser.containsApiKey(apiKey)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_APP_KEY").build());
            return;
        }

        if (StringUtils.isEmpty(apiVersion)) {
            apiVersion = HkAPI.CURRENT_VERSION;
        }

        LocaleContextHolder.getLocaleContext().setApiVersion(apiVersion);

        /**
         * validate token and api key in auth service, do all in memory
         */
        try {
            getHkAuthService().validateToken(authToken, apiKey, false);
        } catch (Throwable t) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_AUTH_TOKEN").build());
            return;
        }

    }

    public HkAuthService getHkAuthService() {
        if (hkAuthService == null) {
            hkAuthService = (HkAuthService) ServiceLocatorFactory.getService(HkAuthService.class);
        }
        return hkAuthService;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
