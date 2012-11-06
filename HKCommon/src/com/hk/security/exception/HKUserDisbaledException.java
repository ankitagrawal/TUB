package com.hk.security.exception;

public class HkUserDisbaledException extends HkAuthenticationException{

    public HkUserDisbaledException(String userName) {
        super(userName);
        // TODO Auto-generated constructor stub
    }

}
