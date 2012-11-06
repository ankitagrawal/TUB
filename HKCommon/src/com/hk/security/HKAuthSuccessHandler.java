package com.hk.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HkAuthSuccessHandler {

    /**
     * determine target url from appuser or request and redirect to target url with token generated.
     * @param request
     * @param response
     * @param authResult
     */
    
    /**
     * 
     * 
     * base64( emailid:expiredon:           b=token->md5(emailid+passwd+expiration)
     *          a                :b
     *          
     *          get pwd for email generate b 
     * il
     */
    public void handleAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, HkAuthentication authResult) throws IOException ;
}
