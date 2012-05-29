package com.hk.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.MapIndia;
import com.hk.pact.dao.location.MapIndiaDao;
import com.hk.service.ServiceLocatorFactory;

/**
 * Created by IntelliJ IDEA. User: USER Date: Oct 21, 2011 Time: 5:12:08 PM To change this template use File | Settings |
 * File Templates.
 */
public class ServiceCityFilter implements Filter {
   // private static org.slf4j.Logger logger      = LoggerFactory.getLogger(ServiceCityFilter.class);

    String                          city;
    MapIndiaDao                     mapIndiaDao = ServiceLocatorFactory.getService(MapIndiaDao.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (httpRequest.getCookies() != null) {
            for (Cookie cookie : httpRequest.getCookies()) {
                if (cookie.getName().equals(HealthkartConstants.Cookie.preferredZone)) {
                    MapIndia city = mapIndiaDao.findByCity(cookie.getValue());
                    if (cookie.getValue() != null && city != null && city.isTargetCity() != null && city.isTargetCity()) {
                        httpRequest.getSession().setAttribute(HealthkartConstants.Cookie.preferredZone, cookie.getValue());
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
