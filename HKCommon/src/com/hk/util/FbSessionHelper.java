package com.hk.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.akube.framework.util.BaseUtils;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookXmlRestClient;
import com.google.code.facebookapi.IFacebookRestClient;
import com.hk.constants.FbConstants;

public class FbSessionHelper {

  private static Logger logger = LoggerFactory.getLogger(FbSessionHelper.class);

  public static FacebookXmlRestClient getUserClient(HttpSession session) {
    return (FacebookXmlRestClient) session.getAttribute(FbConstants.Session.fbUserClient);
  }

  public static int getFriendCountForLoggedInUser(HttpSession session) {
    IFacebookRestClient<Document> userClient = getUserClient(session);
    int friendCount = 0;
    try {
      friendCount = userClient.friends_get().getElementsByTagName("uid").getLength();
    } catch (FacebookException e) {
      try {
        logger.error("Error while getting friend count for uid "+userClient.users_getLoggedInUser());
      } catch (FacebookException e1) {
        logger.error("It seems friend count could not be retreived because no user was logged in");
      }
    }
    return friendCount;
  }


  public static String getCookieParam(HttpServletRequest request, String appId, String param) {
    String cookieName = "fbs_"+ appId;
    Cookie cookie = BaseUtils.getCookie(request, cookieName);
    String value = null;
    if (cookie != null) {
      Map<String,List<String>> paramMap = BaseUtils.getQueryParamMap(cookie.getValue());
      List<String> values = paramMap.get(param);
      if (values != null && !values.isEmpty()) {
        value = values.get(0);
      }
    }
    return value;
  }

}
