package com.hk.security;

import com.hk.security.exception.HKAuthenticationException;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HKAuthService {

    
    HKAuthentication authenticate(HKAuthentication authentication) throws HKAuthenticationException;
}
