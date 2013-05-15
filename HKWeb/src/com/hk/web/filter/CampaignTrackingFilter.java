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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.commons.lang.StringUtils;
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

    private TrafficTrackingDao trafficTrackingDao;
    private TrafficAndUserBrowsingService trafficAndUserBrowsingService;
    private UserService         userService;

    // private static org.slf4j.Logger logger = LoggerFactory.getLogger(CampaignTrackingFilter.class);

    private Boolean     newSession;

    public void init(FilterConfig filterConfig) throws ServletException {
        trafficTrackingDao = ServiceLocatorFactory.getService(TrafficTrackingDao.class);
        trafficAndUserBrowsingService = ServiceLocatorFactory.getService(TrafficAndUserBrowsingService.class);
        userService = ServiceLocatorFactory.getService(UserService.class);
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
	    HttpSession httpSession = httpRequest.getSession();
	    newSession = (Boolean) httpSession.getAttribute(HttpRequestAndSessionConstants.NEW_SESSION);

        User user = getPrincipalUser();
	    String trackingId = null;
        //Get temp user from cookie
	    if (httpRequest.getCookies() != null) {
		    for (Cookie cookie : httpRequest.getCookies()) {
			/*    if (user == null && cookie.getName() != null && cookie.getName().equals(HealthkartConstants.Cookie.tempHealthKartUser)) {
				    String userHash = cookie.getValue();
				    user = userService.findByUserHash(userHash);
				    if (user != null) {
					    logger.debug("Getting Temp User from Cookie and Setting as Principal User=" + userHash);
					    new PrincipalImpl(user);
				    }
			    } else */
                if (cookie.getName() != null && cookie.getName().equals(HealthkartConstants.Cookie.trackingId)) {
				    trackingId = cookie.getValue();
			    }
		    }
	    }

        if (newSession == null || !newSession.equals(true)) {
	        httpSession.setAttribute(HttpRequestAndSessionConstants.NEW_SESSION, true);
	        
	        if (StringUtils.isNotBlank(trackingId)) {
		        TrafficTracking trafficTracking = trafficTrackingDao.get(TrafficTracking.class, Long.valueOf(trackingId));
		        if (trafficTracking != null)
			        httpSession.setAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING_ID, trackingId);
			        httpSession.setAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING, trafficTracking);

	        } else {
		        String userAgent = httpRequest.getHeader(HttpRequestAndSessionConstants.USER_AGENT);
		        //Check if it is a crawler or a bot
		        if (userAgent != null && !userAgent.equals("")
				        && !userAgent.toLowerCase().contains("bot") && !userAgent.toLowerCase().contains("spider")
				        && !userAgent.toLowerCase().contains("price") && !userAgent.toLowerCase().contains("monit/4.10.1") ) {
			        TrafficTracking trafficTracking = trafficAndUserBrowsingService.saveTrafficTracking(httpRequest, user);
			        if (trafficTracking != null && trafficTracking.getId() != null) {
                httpSession.setAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING_ID, trafficTracking.getId());
				        httpSession.setAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING, trafficTracking);
				        Cookie cookie = new Cookie(HealthkartConstants.Cookie.trackingId, trafficTracking.getId().toString());
				        cookie.setPath("/");
				        cookie.setMaxAge(24 * 60 * 60); // 24 hours
				        HttpServletResponse httpResponse = (HttpServletResponse) response;
				        httpResponse.addCookie(cookie);
			        }
		        }
	        }

	        //To use is for Order
            httpSession.setAttribute(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID, ReferrerIds.get(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID));
            httpSession.setAttribute(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID, ReferrerIds.get(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID));
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
