package com.hk.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.user.User;
import com.hk.domain.analytics.TrafficTracking;
import com.hk.pact.dao.marketing.CampaignTrackingDao;
import com.hk.pact.dao.analytics.TrafficTrackingDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.analytics.TrafficAndUserBrowsingService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.OrderSourceFinder;
import com.shiro.PrincipalImpl;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Nov 18, 2011 Time: 3:42:11 PM To change this template use File |
 * Settings | File Templates.
 */
public class CampaignTrackingFilter implements Filter {

    private static final String PING_URL = "http://www.healthkart.com/hello.jsp";

    private static Logger logger = LoggerFactory.getLogger(CampaignTrackingFilter.class);

    private CampaignTrackingDao campaignTrackingDao;
    private TrafficAndUserBrowsingService trafficAndUserBrowsingService;
    private UserService         userService;

    // private static org.slf4j.Logger logger = LoggerFactory.getLogger(CampaignTrackingFilter.class);

    private Boolean     newSession;

    public void init(FilterConfig filterConfig) throws ServletException {
        campaignTrackingDao = (CampaignTrackingDao) ServiceLocatorFactory.getService(CampaignTrackingDao.class);
        trafficAndUserBrowsingService = ServiceLocatorFactory.getService(TrafficAndUserBrowsingService.class);
        userService = (UserService) ServiceLocatorFactory.getService(UserService.class);
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        String requestURL = httpRequest.getRequestURL().toString();
        
        if(requestURL.equals(PING_URL)){
            return ;
        }
        

        Map<String, Long> ReferrerIds = OrderSourceFinder.getOrderReferrer(httpRequest);
        newSession = (Boolean) httpRequest.getSession().getAttribute(HttpRequestAndSessionConstants.NEW_SESSION);

        User user = getPrincipalUser();
        //Get temp user from cookie
        if (user == null && httpRequest.getCookies() != null) {
          for (Cookie cookie : httpRequest.getCookies()) {
            if (cookie.getName() != null && cookie.getName().equals(HealthkartConstants.Cookie.tempHealthKartUser)) {
              String userHash = cookie.getValue();
              user = userService.findByUserHash(userHash);
              logger.debug("Getting Temp User from Cookie and Setting as Principal User="+user.getUserHash());
              new PrincipalImpl(user);
            }
          }
        }

        if (newSession == null || !newSession.equals(true)) {
	        httpRequest.getSession().setAttribute(HttpRequestAndSessionConstants.NEW_SESSION, true);

	        /*String referrer = httpRequest.getHeader(HttpRequestAndSessionConstants.REFERER);
	        String utm_source = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_SOURCE);
	        String utm_campaign = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_CAMPAIGN);
	        String utm_medium = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_MEDIUM);
	        campaignTrackingDao.saveRequest(
			        referrer != null ? referrer : null, httpRequest.getRequestURL().toString(), utm_source, utm_medium, utm_campaign,
			        user);*/
	        
	        TrafficTracking trafficTracking = trafficAndUserBrowsingService.saveTrafficTracking(httpRequest, user);
			httpRequest.getSession().setAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING, trafficTracking);

	        //To use is for Order
            httpRequest.getSession().setAttribute(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID, ReferrerIds.get(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID));
            httpRequest.getSession().setAttribute(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID, ReferrerIds.get(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID));
        }
        chain.doFilter(request, response);

    }

    public PrincipalImpl getPrincipal() {
        return (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
    }

    public User getPrincipalUser() {
        if (getPrincipal() == null)
            return null;
        return userService.getUserById(getPrincipal().getId());
    }

    

    public void destroy() {
    }
}
