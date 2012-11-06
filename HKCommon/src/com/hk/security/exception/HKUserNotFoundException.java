package com.hk.security.exception;

public class HkUserNotFoundException extends HkAuthenticationException{

    public HkUserNotFoundException(String userName) {
        super(userName);
        
    }

}
