package com.hk.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebContext {

  private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
  private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();

  private WebContext() {}

  public static void setContext(ServletRequest servletRequest, ServletResponse servletResponse) {
    HttpServletRequest httpServletRequest = null;
    HttpServletResponse httpServletResponse = null;
    try {
      httpServletRequest = (HttpServletRequest) servletRequest;
      httpServletResponse = (HttpServletResponse) servletResponse;
    } catch (Exception e) {
    }
    requestThreadLocal.set(httpServletRequest);
    responseThreadLocal.set(httpServletResponse);
  }

  public static HttpServletRequest getRequest() {
    return requestThreadLocal.get();
  }

  public static HttpServletResponse getResponse() {
    return responseThreadLocal.get();
  }

	public static boolean isSecure() {
		String isSecureString = getRequest().getHeader("x-proto");
		return isSecureString != null ? isSecureString.equals("SSL") : false;
	}
}
