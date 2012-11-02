package com.hk.security.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.security.HKAuthSuccessHandler;
import com.hk.security.HKAuthentication;

public class HKAuthSuccessHandlerImpl implements HKAuthSuccessHandler{

    @Override
    public void handleAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, HKAuthentication authResult) throws IOException {
        String targetUrl = determineTargetUrl(request, response);
        
        
        String redirectUrl = calculateRedirectUrl(request.getContextPath(), targetUrl);
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        

        response.sendRedirect(redirectUrl);
        
    }
    
    private String calculateRedirectUrl(String contextPath, String url) {
        boolean contextRelative = false;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (contextRelative) {
                return url;
            } else {
                return contextPath + url;
            }
        }

        // Full URL, including http(s)://

        if (!contextRelative) {
            return url;
        }

        // Calculate the relative URL from the fully qualifed URL, minus the protocol and base context.
        url = url.substring(url.indexOf("://") + 3); // strip off protocol
        url = url.substring(url.indexOf(contextPath) + contextPath.length());

        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }

        return url;
    }
    
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
      return "http://www.google.com";  
    }

}
