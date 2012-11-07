package com.hk.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.api.AuthAPI;
import com.hk.api.HkAPI;
import com.hk.api.user.HkAPIUser;
import com.hk.security.HkAuthService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.json.JSONResponseBuilder;
import com.hk.web.servlet.constants.OperationType;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class HKAPIServlet extends HkAPIBaseServlet {

    private static Logger       logger          = LoggerFactory.getLogger(HKAPIServlet.class);

    private final static String CURRENT_VERSION = "1.0";

    public enum Operation {
        GetUserDetails, ValidateAndRefreshAuthToken
    }

    private HkAuthService                           hkAuthService;

    private static Map<Integer, Map<String, HkAPI>> apiRegistry = new HashMap<Integer, Map<String, HkAPI>>();

    static {
        Map<String, HkAPI> authAPIVersions = new HashMap<String, HkAPI>();
        AuthAPI authAPI = ServiceLocatorFactory.getService(AuthAPI.class);
        authAPIVersions.put("1.0", authAPI);
        apiRegistry.put(OperationType.Auth, authAPIVersions);

        /*
         * Map<String, HkAPI> userAPIVersions = new HashMap<String, HkAPI>(); userAPIVersions.put("1.0", new
         * HotelSearchAPIImpl()); apiRegistry.put(OperationType.USER, userAPIVersions);
         */

    }

    @Override
    protected final void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String authToken = req.getParameter("authToken");
        String apiVersion = req.getParameter("apiVersion");
        String appId = req.getParameter("appId");

        String operationTypeStr = req.getParameter("type");

        String operationStr = req.getParameter("operation");

        if (StringUtils.isBlank(operationStr)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_OPERATION_PASSED").build());
            return;
        }

        Operation operation = Operation.valueOf(operationStr);
        // it should never be null
        if (operation == null) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "REQUEST_NOT_SUPPORTED").build());
            return;
        }

        if (StringUtils.isBlank(operationTypeStr)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_OPERATION_TYPE_PASSED").build());
            return;
        }

        int operationType = Integer.parseInt(operationTypeStr);
        // check for a not null apiKey
        if (StringUtils.isEmpty(authToken)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_AUTH_TOKEN_PASSED").build());
            return;
        }

        if (StringUtils.isEmpty(appId)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_APP_ID_PASSED").build());
            return;
        }
        
        if(!HkAPIUser.containsAppId(appId)){
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_APP_ID").build());
            return; 
        }

        // check for a not null apiKey
        if (!StringUtils.isEmpty(apiVersion)) {

            if (!apiRegistry.get(operationType).containsKey(apiVersion)) {
                writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "UNSUPPORTED_API_VERSION").build());
                return;
            }

        } else {
            apiVersion = CURRENT_VERSION;
        }

        /**
         * validate token and api key in auth service, do all in memory
         */
        try {
            getHkAuthService().validateToken(authToken, appId, false);
        } catch (Throwable t) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_AUTH_TOKEN").build());
            return;
        }
        

        switch (operationType) {
            case OperationType.Auth:
                AuthAPI authAPI = (AuthAPI) apiRegistry.get(OperationType.Auth).get(apiVersion);
                try {
                    processAuthRequest(authAPI, req, resp, operation);
                } catch (Throwable th) {
                    logger.error("Exception occured while servicing api request:", th);
                    writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INTERNAL_SERVER_ERROR").build());
                }
                break;

        }
    }

    private void processAuthRequest(AuthAPI authAPI, HttpServletRequest req, HttpServletResponse resp, Operation operation) {
        switch (operation) {
            case ValidateAndRefreshAuthToken:
                String authToken = req.getParameter("authToken");
                String appId = req.getParameter("appId");

                writeSuccessResponse(req, resp, authAPI.validateAndRefreshAuthToken(authToken, appId, null));
                break;
        }
    }

    public HkAuthService getHkAuthService() {
        if (hkAuthService == null) {
            hkAuthService = (HkAuthService) ServiceLocatorFactory.getService(HkAuthService.class);
        }
        return hkAuthService;
    }

}