package com.akube.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

    httpResponse.sendRedirect("http://admin.healthkart.com/admin");
  }


  public void init(FilterConfig config) throws ServletException {}

  
  public void destroy() {}
}

