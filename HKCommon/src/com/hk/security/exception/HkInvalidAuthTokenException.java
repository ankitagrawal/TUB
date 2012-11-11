package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

public class HkInvalidAuthTokenException extends HealthkartRuntimeException{

    
    private static final long serialVersionUID = 1L;
    private String            authToken;

    public HkInvalidAuthTokenException(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("invalid auth Token " + authToken);
        return message.toString();
    }
}
