package com.hk.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.stereotype.Component;

import com.hk.service.ServiceLocatorFactory;
import com.shiro.PrincipalImpl;

@Component
public class RememberMeFilter implements Filter {

    private DefaultWebSecurityManager securityManager;
    private SecurityUtils             securityUtils;

    public void init(FilterConfig filterConfig) throws ServletException {
        securityManager = (DefaultWebSecurityManager) ServiceLocatorFactory.getService("SecurityManager");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (getSecurityUtils().getSubject().isRemembered()) {
            @SuppressWarnings("unused")
            PrincipalImpl principal = (PrincipalImpl) securityUtils.getSubject().getPrincipal();
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

    public SecurityUtils getSecurityUtils() {
        return securityUtils;
    }

    public void setSecurityUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }
}
