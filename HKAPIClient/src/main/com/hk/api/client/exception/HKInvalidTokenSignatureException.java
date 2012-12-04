package com.hk.api.client.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 5:14 PM
 */
public class HKInvalidTokenSignatureException extends HKAPIBaseException {
    private static final long serialVersionUID = 1L;
    private String            token;

    public HKInvalidTokenSignatureException(String token) {
        this.token = token;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append(" token expired " + token);
        return message.toString();
    }
}

