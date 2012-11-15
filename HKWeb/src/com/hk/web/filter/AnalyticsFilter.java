package com.hk.web.filter;

import com.hk.service.ServiceLocatorFactory;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderService;
import com.hk.constants.core.HealthkartConstants;
import com.shiro.PrincipalImpl;
import com.akube.framework.dao.Page;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.shiro.SecurityUtils;

public class AnalyticsFilter implements Filter {

  private UserService userService;
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
    HttpServletResponse httpResponse = (HttpServletResponse) response;

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

  public User getPrincipalUser() {
      if (getPrincipal() == null)
          return null;
      return userService.getUserById(getPrincipal().getId());
  }

}
