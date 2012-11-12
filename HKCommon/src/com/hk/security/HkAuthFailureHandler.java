package com.hk.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.security.exception.HkAuthenticationException;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HkAuthFailureHandler {

    public void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, HkAuthenticationException failed);
    
    
    
}
