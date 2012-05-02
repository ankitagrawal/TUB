package com.hk.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.service.ServiceLocatorFactory;
import com.shiro.PrincipalImpl;

public class RememberMeFilter implements Filter {

    @Autowired
    private DefaultWebSecurityManager securityManager;

    public void init(FilterConfig filterConfig) throws ServletException {
        securityManager = (DefaultWebSecurityManager) ServiceLocatorFactory.getService(org.apache.shiro.mgt.SecurityManager.class);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (getSecurityManager().getSubject().isRemembered()) {
            @SuppressWarnings("unused")
            PrincipalImpl principal = (PrincipalImpl) securityManager.getSubject().getPrincipal();
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }

    public DefaultWebSecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(DefaultWebSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

}
