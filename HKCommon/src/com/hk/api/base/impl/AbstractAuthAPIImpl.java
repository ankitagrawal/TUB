package com.hk.api.base.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.api.AuthAPI;
import com.hk.security.HkAuthService;
import com.hk.security.exception.HKAuthTokenExpiredException;
import com.hk.security.exception.HkInvalidAuthTokenException;
import com.hk.util.json.JSONResponseBuilder;


public abstract class AbstractAuthAPIImpl implements AuthAPI {

    @Autowired
    private HkAuthService hkAuthService;

    @Override
    public String validateAndRefreshAuthToken(String authToken, String appId, String authScheme) {
        boolean isExpired = false, isValid = true;

        try {
            isValid = getHkAuthService().validateToken(authToken);
        } catch (HKAuthTokenExpiredException e) {
            isValid = false;
            isExpired = true;
        } catch (HkInvalidAuthTokenException e) {
            isValid = false;
        }

        if (isExpired && isValid) {
            authToken = getHkAuthService().refershAuthToken(authToken, appId);
        }

        return new JSONResponseBuilder().addField("valid", isValid).addField("expired", isExpired).addField("authToken", authToken).build();

    }

    public HkAuthService getHkAuthService() {
        return hkAuthService;
    }

}
