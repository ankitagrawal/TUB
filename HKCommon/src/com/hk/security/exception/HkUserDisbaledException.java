package com.hk.security.exception;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class HkUserDisbaledException extends HkAuthenticationException{

    
    private static final long serialVersionUID = 1L;
    private String userName;
    
    public HkUserDisbaledException(String userName) {
        this.userName = userName;
    }
    
    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("user  " + userName + " is disabled");
        return message.toString();
    }

}
