package com.hk.security;

import com.hk.security.exception.HkAuthenticationException;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HkAuthService {
    
    public static final int DEFAULT_EXPIRY = 3600;

    
    public HkAuthentication authenticate(HkAuthentication authentication) throws HkAuthenticationException;
    
    public String generateAuthToken(HkAuthentication authentication);
}
