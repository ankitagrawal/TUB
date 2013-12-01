package com.hk.security.exception;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class HkUserNotFoundException extends HkAuthenticationException {

    
    private static final long serialVersionUID = 1L;
    private String userName;

    public HkUserNotFoundException(String userName) {
        this.userName = userName;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append(" no user  " + userName + " exists in system");
        return message.toString();
    }
}
