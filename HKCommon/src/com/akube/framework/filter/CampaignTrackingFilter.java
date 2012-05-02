package com.akube.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Nov 18, 2011
 * Time: 3:42:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignTrackingFilter implements Filter {
 /* CampaignTrackingDao campaignTrackingDao = ServiceLocatorFactory.getService(CampaignTrackingDao.class);
  UserDao userDaoProvider = ServiceLocatorFactory.getService(UserDao.class);

  private static org.slf4j.Logger logger = LoggerFactory.getLogger(CampaignTrackingFilter.class);*/

//  @Session(key = HealthkartConstants.Session.newSession)
//  private Boolean newSession;


  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

   /* if (!(request instanceof HttpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    String utm_source = httpRequest.getParameter("utm_source");
    String utm_campaign = httpRequest.getParameter("utm_campaign");
    String utm_medium = httpRequest.getParameter("utm_medium");

    //if its a new session
    Object newSession = httpRequest.getSession().getAttribute("newSession");
    if(newSession == null || !newSession.equals(true)) {
      String referrer = httpRequest.getHeader("referer");
       campaignTrackingDao.saveRequest(referrer != null ? referrer : null, httpRequest.getRequestURL().toString(), utm_source, utm_medium, utm_campaign, getPrincipalUser());
       httpRequest.getSession().setAttribute("newSession", true);
       httpRequest.getSession().setAttribute("referrer", referrer);
    }
    chain.doFilter(request, response);*/
  }

 /* public PrincipalImpl getPrincipal() {
    return (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
  }

  public User getPrincipalUser() {
    if (getPrincipal() == null) return null;
    return userDaoProvider.find(getPrincipal().getId());
  }*/

  public void init(FilterConfig config) throws ServletException {}

  public void destroy() {}
}

