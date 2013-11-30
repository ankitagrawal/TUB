package com.akube.framework.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class AdminFilter implements Filter {

  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    // skip non-http requests
    if (!(request instanceof HttpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletResponse httpResponse = (HttpServletResponse) response;

    httpResponse.sendRedirect("http://btadmin.healthkart.com/admin");
  }


  public void init(FilterConfig config) throws ServletException {}

  
  public void destroy() {}
}

