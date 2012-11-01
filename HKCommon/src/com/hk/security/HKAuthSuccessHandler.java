package com.hk.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HKAuthSuccessHandler {

    
    public void handleAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, HKAuthentication authResult);
}
