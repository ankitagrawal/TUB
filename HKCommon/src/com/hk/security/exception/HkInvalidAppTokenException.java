package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 11/22/12
 */
public class HkInvalidAppTokenException extends HealthkartRuntimeException{
    private static final long serialVersionUID = 1L;
    private String            appToken;

    public HkInvalidAppTokenException(String appToken) {
        this.appToken = appToken;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("invalid apptoken: " + appToken);
        return message.toString();
    }
}
