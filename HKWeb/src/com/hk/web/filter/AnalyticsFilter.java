package com.hk.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;

import com.akube.framework.dao.Page;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderService;
import com.hk.service.ServiceLocatorFactory;
import com.shiro.PrincipalImpl;

public class AnalyticsFilter implements Filter {

    private UserService  userService;
    private OrderService orderService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = (UserService) ServiceLocatorFactory.getService(UserService.class);
        orderService = (OrderService) ServiceLocatorFactory.getService(OrderService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // HttpServletResponse httpResponse = (HttpServletResponse) response;

        Boolean orderCountSet = (Boolean) httpRequest.getSession().getAttribute(HealthkartConstants.Session.orderCountSetBoolean);
        if (orderCountSet == null || !orderCountSet) {
            User user = getPrincipalUser();
            int orderCount = 0;

            if (user != null) {
                Page orderPage = orderService.listOrdersForUser(user, 1, 1);
                orderCount = orderPage.getTotalResults();

                // set order count to session var
                httpRequest.getSession().setAttribute(HealthkartConstants.Session.orderCount, orderCount);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    public PrincipalImpl getPrincipal() {
        return (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
    }

    private User getPrincipalUser() {
        if (getPrincipal() == null)
            return null;
        return userService.getUserById(getPrincipal().getId());
        // return UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
    }

}
