package com.hk.impl.service;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.hk.constants.edge.ServiceEndPoints;
import com.hk.constants.edge.StoreConstants;
import com.hk.pact.service.UserSessionService;
import com.hk.util.http.HkHttpClient;
import com.hk.util.http.URIBuilder;
import com.shiro.PrincipalImpl;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    private static final String LOGIN  = "login/";
    private static final String LOGOUT = "logout/";
    private static final String AUTH   = "auth/";

    @Override
    public boolean isUserAuthenticated(Long userId) {

        PrincipalImpl loggedInPrincipalImpl = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();

        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.USER_SESSION + AUTH + StoreConstants.DEFAULT_STORE_ID_STR + URIBuilder.URL_TOKEN_SEP
                + loggedInPrincipalImpl.getId().toString());
        GenericResponseWrapperFromEdge genericResponseWrapperFromEdge = (GenericResponseWrapperFromEdge) HkHttpClient.executeGet(builder.getWebServiceUrl(),
                GenericResponseWrapperFromEdge.class);

        if (genericResponseWrapperFromEdge != null && genericResponseWrapperFromEdge.getGenericResponseFromEdge() != null) {
            return genericResponseWrapperFromEdge.getGenericResponseFromEdge().isException();
        }

        return false;

    }

    @Override
    public void onLoginUser() {
        PrincipalImpl loggedInPrincipalImpl = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();

        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.USER_SESSION + LOGIN + StoreConstants.DEFAULT_STORE_ID_STR + URIBuilder.URL_TOKEN_SEP
                + loggedInPrincipalImpl.getId().toString());
        HkHttpClient.executeGet(builder.getWebServiceUrl());

    }

    @Override
    public void onLogoutUser() {
        PrincipalImpl loggedInPrincipalImpl = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();

        if (loggedInPrincipalImpl != null && loggedInPrincipalImpl.getId() != null) {
            URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.USER_SESSION + LOGOUT + StoreConstants.DEFAULT_STORE_ID_STR + URIBuilder.URL_TOKEN_SEP
                    + loggedInPrincipalImpl.getId().toString());
            HkHttpClient.executeGet(builder.getWebServiceUrl());
        }

    }

}
