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
import javax.servlet.http.HttpServletResponse;

import com.hk.domain.affiliate.Affiliate;
import net.sourceforge.stripes.util.CryptoUtil;

import com.hk.constants.core.HealthkartConstants;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.affiliate.AffiliateTrafficDetailsDao;
import com.hk.service.ServiceLocatorFactory;

/**
 * Dont Delete!!
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Aug 12, 2011
 * Time: 1:10:38 AM
 */
public class AffiliateTrackingFilter implements Filter {

  AffiliateDao affiliateDao = ServiceLocatorFactory.getService(AffiliateDao.class);
  AffiliateTrafficDetailsDao affiliateTrafficDetailsDao = ServiceLocatorFactory.getService(AffiliateTrafficDetailsDao.class);
  //private static org.slf4j.Logger logger = LoggerFactory.getLogger(AffiliateTrackingFilter.class);

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    if (!(request instanceof HttpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String affid = httpRequest.getParameter("affid");

    boolean alreadyContainsValidAffiliateCookie = false;

    //if url has affiliate id
    if (affid != null) {
      //read its present cookies
      if (httpRequest.getCookies() != null) {
        for (Cookie cookie : httpRequest.getCookies()) {
          //check if user already has an associated affiliate
          if (cookie.getName().equals(HealthkartConstants.Cookie.affiliate_id) && affiliateDao.getAffiliateByCode(affid) != null) {
//            logger.info("affiliate_cookie " +cookie.getName() + CryptoUtil.decrypt(cookie.getValue()));
            alreadyContainsValidAffiliateCookie = true;
          }
          // if yes, is he coming again from the same affiliate, if yes reset maxage, if no remove the earlier cookie, add the latest one
          if (alreadyContainsValidAffiliateCookie) {
            if (cookie.getValue() != null && !CryptoUtil.decrypt(cookie.getValue()).equalsIgnoreCase(affid)) {
              //deleting previous cookie
//              cookie.setMaxAge(0);
//              httpResponse.addCookie(cookie);
              // adding new cookie for latest affiliate
              Cookie affiliateCookie = new Cookie(HealthkartConstants.Cookie.affiliate_id, CryptoUtil.encrypt(affid));
              affiliateCookie.setPath("/");
              affiliateCookie.setMaxAge(15 * 24 * 3600);
              httpResponse.addCookie(affiliateCookie);
            } else {
              // reset cookie maxage again to 15 days
//              cookie.setMaxAge(0);
//              httpResponse.addCookie(cookie);
              Cookie affiliateCookie = new Cookie(HealthkartConstants.Cookie.affiliate_id, CryptoUtil.encrypt(affid));
              affiliateCookie.setPath("/");
              affiliateCookie.setMaxAge(15 * 24 * 3600);
              httpResponse.addCookie(affiliateCookie);
            }
            break;
          }
        }
      }

	    Affiliate affiliate = affiliateDao.getAffiliateByCode(affid);

	    // monitoring traffic for affiliate id
	    if (affiliate != null) {
		    affiliateTrafficDetailsDao.saveTrafficDetails(affiliate, httpRequest.getRequestURL() != null ? httpRequest.getRequestURL().toString() : "", httpRequest.getHeader("referer") != null ? httpRequest.getHeader("referer") : null);
	    }

	    // for users coming first time through an affiliate
      if (!alreadyContainsValidAffiliateCookie) {
        Cookie affiliateCookie = new Cookie(HealthkartConstants.Cookie.affiliate_id, CryptoUtil.encrypt(affid));
        affiliateCookie.setPath("/");
        affiliateCookie.setMaxAge(15 * 24 * 3600);
        httpResponse.addCookie(affiliateCookie);
      }
    }
    chain.doFilter(request, response);
  }

  public void init(FilterConfig config) throws ServletException {}

  public void destroy() {}
}

