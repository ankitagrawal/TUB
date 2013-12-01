package com.hk.web.action.core.catalog.category;

import javax.servlet.http.Cookie;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.lang.StringUtils;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.filter.WebContext;

public class ServiceAction extends BaseAction {
    @Session(key = HealthkartConstants.Cookie.preferredZone)
    private String preferredZone;

    private String redirectUrl;

    @DefaultHandler
    public Resolution setCookie() {
      Cookie preferredZoneCookie = new Cookie(HealthkartConstants.Cookie.preferredZone, preferredZone);
      preferredZoneCookie.setPath("/");
      preferredZoneCookie.setMaxAge(10 * 365 * 24 * 3600);
      WebContext.getResponse().addCookie(preferredZoneCookie);
      WebContext.getResponse().setContentType("text/html");
      if (!StringUtils.isBlank(redirectUrl)) {
        return new RedirectResolution(redirectUrl, false);
      }
      return new ForwardResolution(CategoryAction.class, "pre").addParameter("category", "services");
    }

    public Resolution setDefaultCookie() {
      Cookie preferredZoneCookie = new Cookie(HealthkartConstants.Cookie.preferredZone, "Delhi");
      preferredZoneCookie.setPath("/");
      preferredZoneCookie.setMaxAge(10 * 365 * 24 * 3600);
      WebContext.getResponse().addCookie(preferredZoneCookie);
      WebContext.getResponse().setContentType("text/html");
      HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success");
      return new JsonResolution(healthkartResponse);
    }

    public String getRedirectUrl() {
      return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
      this.redirectUrl = redirectUrl;
    }

    public String getPreferredZone() {
      return preferredZone;
    }

    public void setPreferredZone(String preferredZone) {
      this.preferredZone = preferredZone;
    }
  }
