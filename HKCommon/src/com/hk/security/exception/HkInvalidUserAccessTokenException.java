package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 11/22/12
 */
public class HkInvalidUserAccessTokenException extends HealthkartRuntimeException {
    private static final long serialVersionUID = 1L;
    private String            userAccessToken;

    public HkInvalidUserAccessTokenException(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("invalid user access token: " + userAccessToken);
        return message.toString();
    }
}
