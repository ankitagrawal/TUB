package com.hk.security;

import com.hk.security.exception.HKAuthTokenExpiredException;
import com.hk.security.exception.HkAuthenticationException;
import com.hk.security.exception.HkInvalidAuthTokenException;

/**
 * @author vaibhav.adlakha
 */
public interface HkAuthService {

    public static final int    DEFAULT_EXPIRY_MIN = 1;
    public static final String AUTH_FAILURE_URL   = "/nutrition";

    public HkAuthentication authenticate(HkAuthentication authentication) throws HkAuthenticationException;

    public String generateAuthToken(HkAuthentication authentication);

    //public boolean validateToken(String authToken, String appId) throws HkInvalidAuthTokenException, HKAuthTokenExpiredException;

    public boolean validateToken(String authToken, String appId,boolean validatePwd)throws HkInvalidAuthTokenException, HKAuthTokenExpiredException ; 
    
    // TODO: add a auth scheme to this method
    public String refershAuthToken(String authToken, String appId);
}
