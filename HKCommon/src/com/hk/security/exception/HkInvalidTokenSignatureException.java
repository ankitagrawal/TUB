package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 11/25/12
 * Time: 3:32 PM
 */
public class HkInvalidTokenSignatureException extends HealthkartRuntimeException {
    private static final long serialVersionUID = 1L;
    private String            token;

    public HkInvalidTokenSignatureException(String token) {
        this.token = token;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append(" token expired " + token);
        return message.toString();
    }
}

