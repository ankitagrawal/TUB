package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

/**
 * @author vaibhav.adlakha
 */
public class HkAuthenticationException extends HealthkartRuntimeException {

    private static final long serialVersionUID = 1L;
    private String            userName;

    public HkAuthenticationException() {
        super();
    }

    public HkAuthenticationException(String userName) {
        this.userName = userName;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder(super.getMessage() != null ? super.getMessage() : "");
        message.append("Could not authenticate " + userName + " with given credentials");
        return message.toString();
    }

}
