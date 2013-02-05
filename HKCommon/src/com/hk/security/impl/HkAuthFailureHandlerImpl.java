package com.hk.security.impl;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;

import org.springframework.stereotype.Service;

import com.hk.security.HkAuthFailureHandler;
import com.hk.security.exception.HkAuthenticationException;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;

@Service
public class HkAuthFailureHandlerImpl implements HkAuthFailureHandler {

    @Override
    public void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, HkAuthenticationException failed) {
        request.setAttribute("error", failed.getMessage());

        //try {
            System.out.println(request.getAttribute("error"));
            try {
                response.sendRedirect(getFailureUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            /request.getRequestDispatcher(getFailureUrl()).forward(request, response);
        
        /*} catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public String getFailureUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/pages/singleLogin.jsp");
        return getUrlFromResolution(redirectResolution);
    }

    private String getUrlFromResolution(RedirectResolution redirectResolution) {
        String url = redirectResolution.getUrl(Locale.getDefault());
        if (WebContext.getRequest() != null && WebContext.getResponse() != null) {
            return SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), url, null);
        }
        String contextPath = AppConstants.contextPath;
        return SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), url, contextPath);
    }

}
