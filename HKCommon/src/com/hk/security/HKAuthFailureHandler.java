package com.hk.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.security.exception.HKAuthenticationException;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HKAuthFailureHandler {

    public void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, HKAuthenticationException failed);
    
    
    
}
