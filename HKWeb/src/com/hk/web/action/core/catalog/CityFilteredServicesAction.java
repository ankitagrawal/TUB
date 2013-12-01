package com.hk.web.action.core.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.HealthkartConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.filter.WebContext;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Oct 21, 2011
 * Time: 4:13:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class CityFilteredServicesAction extends BaseAction implements ValidationErrorHandler {

  String city;

  List<String> featuredCityList = new ArrayList<String>();

  public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
    return new JsonResolution(validationErrors, getContext().getLocale());
  }

  public Resolution pre() {
    Cookie preferredZoneCookie = BaseUtils.getCookie(WebContext.getRequest(), HealthkartConstants.Cookie.preferredZone);
    if (preferredZoneCookie == null) {
      preferredZoneCookie = new Cookie(HealthkartConstants.Cookie.affiliate_id, CryptoUtil.encrypt(city));
      preferredZoneCookie.setPath("/");
      preferredZoneCookie.setMaxAge(365 * 24 * 3600);
      WebContext.getResponse().addCookie(preferredZoneCookie);
      WebContext.getResponse().setContentType("text/html");
    } else {

    }

    return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Default City Changed",city));
  }
}

