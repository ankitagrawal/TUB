package com.hk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 2/7/12
 * Time: 12:43 AM
 * To change this template use File | Settings | File Templates.
 */


public class HkLoginAuthenticationUtil {
  private static Logger LOGGER = LoggerFactory.getLogger(HkLoginAuthenticationUtil.class);

  public static Cookie[] authenticateHttpSession(String loginUrl, String submitUrl, Map<String, String> paramNameValueMap) {
    try {
      HttpClient httpClient = new HttpClient();
      HttpState httpState = new HttpState();
      httpClient.setState(httpState);
      HostConfiguration hostconfig = new HostConfiguration();
      hostconfig.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
      httpClient.setHostConfiguration(hostconfig);

      GetMethod getMethod = new GetMethod(loginUrl);
      getMethod.addRequestHeader("User-Agent", "HealthKart");
      httpClient.executeMethod(getMethod);
      Cookie[] cookies = httpClient.getState().getCookies();

      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
      for (Map.Entry<String, String> entry : paramNameValueMap.entrySet()) {
        nameValuePairs.add(new NameValuePair(entry.getKey(), entry.getValue()));
      }

      PostMethod postMethod = new PostMethod(submitUrl);
      postMethod.addParameters(nameValuePairs.toArray(new NameValuePair[nameValuePairs.size()]));
      postMethod.addRequestHeader("User-Agent", "HealthKart");
      postMethod.addRequestHeader("Referer", loginUrl);
      getMethod.releaseConnection();
      httpState.addCookies(cookies);
      httpClient.setState(httpState);
      httpClient.executeMethod(postMethod);
      Cookie[] autCookies = httpClient.getState().getCookies();
      postMethod.releaseConnection();
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("HKLoginAuthenticationUtil::Session authenticated. Cookies: " + getCookieString(autCookies));
      }
      return autCookies;
    } catch (Exception e) {
      LOGGER.error("Error while authenticating url " + submitUrl, e);
    }
    return new Cookie[0];
  }

  private static String getCookieString(Cookie[] cookies) {
    if (cookies.length == 0) {
      return "";
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (Cookie cookie : cookies) {
      stringBuilder.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
    }
    String cookieString = stringBuilder.toString();
    return cookieString.substring(0, cookieString.length() - 1);
  }

  public static void main(String[] args) throws Exception {
//      CrawlerUtils.setHttpsRequestConfig();
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("textUsre", "LL191");
    properties.put("textPass", "LL191");
    Cookie[] cookies = authenticateHttpSession("http://cust.dtdc.co.in/default.asp", "http://cust.dtdc.co.in/default.asp", properties);
    String cookie = getCookieString(cookies);
//      UrlFetch urlFetch = new UrlFetch("http://www.singleparentmeet.com/community/index.cfm?",
//              new URLFetchAttributes(2000, 2000, null, "HealthKart", cookie, SiteCrawlCompressionMode.None, false));
//      System.out.println(urlFetch.fetchPage());
  }

}
