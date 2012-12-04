package com.hk.api.client.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 2:19 PM
 */
public class HKInvalidUserAccessTokenException extends HKAPIBaseException {
    private static final long serialVersionUID = 1L;
    private String            userAccessToken;

    public HKInvalidUserAccessTokenException(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("invalid user access token: " + userAccessToken);
        return message.toString();
    }
}