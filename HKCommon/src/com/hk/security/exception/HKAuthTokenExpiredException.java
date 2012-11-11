package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class HKAuthTokenExpiredException extends HealthkartRuntimeException{

    
    private static final long serialVersionUID = 1L;
    private String            authToken;

    public HKAuthTokenExpiredException(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("auth token expired " + authToken);
        return message.toString();
    }
}
