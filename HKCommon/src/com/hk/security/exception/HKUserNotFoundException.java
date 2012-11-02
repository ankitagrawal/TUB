package com.hk.security.exception;

public class HKUserNotFoundException extends HKAuthenticationException{

    public HKUserNotFoundException(String userName) {
        super(userName);
        
    }

}
