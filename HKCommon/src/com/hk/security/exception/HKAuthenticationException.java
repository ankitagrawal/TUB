package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

public class HkAuthenticationException extends HealthkartRuntimeException {

    private static final long serialVersionUID = 1L;
    private String            userName;

    public HkAuthenticationException(String userName) {

        this.userName = userName;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder(super.getMessage());
        message.append("Could not authenticate " + userName + " with given credentials");
        return message.toString();
    }

}
