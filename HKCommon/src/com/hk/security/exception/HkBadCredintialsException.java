package com.hk.security.exception;

import com.hk.exception.HealthkartRuntimeException;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class HkBadCredintialsException extends HealthkartRuntimeException{

    
    private static final long serialVersionUID = 1L;
    private String            userName;
    

    public HkBadCredintialsException(String userName) {
        this.userName = userName;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Invalid Creditantials for userName " + userName);
        return message.toString();
    }
}
