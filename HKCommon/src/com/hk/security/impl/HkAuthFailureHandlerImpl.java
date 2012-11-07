package com.hk.security.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.hk.security.HkAuthFailureHandler;
import com.hk.security.HkAuthService;
import com.hk.security.exception.HkAuthenticationException;

@Service
public class HkAuthFailureHandlerImpl implements HkAuthFailureHandler{

    @Override
    public void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, HkAuthenticationException failed) {
        request.setAttribute("error", failed.getMessage());
        
        try {
            System.out.println(request.getAttribute("error"));
            request.getRequestDispatcher(HkAuthService.AUTH_FAILURE_URL).forward(request, response);
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
