package com.hk.web.action.test;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.web.AppConstants;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.filter.WebContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.ssl.SslUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ajeet
 * Date: 09 Apr, 2013
 */
@Secure(hasAllRoles = {RoleConstants.ADMIN}, authActionBean = AdminPermissionAction.class)
@Component
public class UrlTesterAction extends BaseAction {

  @Autowired
  MenuHelper menuHelper;

  String category;
  String server;


  List<URLRedirect> urlRedirectList = new ArrayList<URLRedirect>();

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/test/urlTester.jsp");
  }

  public Resolution testUrl() {
    List<MenuNode> menuNodes = menuHelper.getMenuNodesFlat();
    for (MenuNode menuNode : menuNodes) {
      if (menuNode.getUrl().startsWith(category)) {
        try {
          String menuNodeUrl = menuNode.getUrl();
          String menuUrl = "";
          if (StringUtils.isNotBlank(server)) {
            menuUrl = server + menuNodeUrl;
          } else {
            menuUrl = SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), menuNodeUrl, AppConstants.contextPath);
          }
          System.out.println("originalUrl->" + menuUrl);
          URL url = new URL(menuUrl);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setReadTimeout(5000);
          connection.setInstanceFollowRedirects(false);

          boolean redirect = false;
          String redirectUrl = "";
          int finalCode = 404;
          int redirectionCode = connection.getResponseCode();
          System.out.println("redirectionCode ->" + redirectionCode);
          if (redirectionCode != HttpURLConnection.HTTP_OK) {
            if (redirectionCode == HttpURLConnection.HTTP_MOVED_TEMP
                || redirectionCode == HttpURLConnection.HTTP_MOVED_PERM
                || redirectionCode == HttpURLConnection.HTTP_SEE_OTHER)
              redirect = true;
          }
          if (redirect) {
            redirectUrl = connection.getHeaderField("Location");
            System.out.println("redirectUrl ->" + redirectUrl);
            connection = (HttpURLConnection) new URL(redirectUrl).openConnection();
            finalCode = connection.getResponseCode();
            System.out.println("finalCode ->" + finalCode);
          }
          URLRedirect urlRedirect = new URLRedirect(menuNodeUrl, redirectionCode, redirectUrl, finalCode);
          urlRedirectList.add(urlRedirect);
          connection.disconnect();
        } catch (IOException e) {
          System.out.println("Unable to connect url: " + e.getMessage());
          e.printStackTrace();
        } 
      }
    }
    return new ForwardResolution("/pages/test/urlTester.jsp");
  }

  public static void main(String[] args) {

    try {

      String url = "http://staging01.healthkart.com/nutrition";

      URL obj = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
      conn.setReadTimeout(5000);
      conn.setInstanceFollowRedirects(false);
      //conn.connect();
      System.out.println("Request URL ... " + url);

      boolean redirect = false;

      // normally, 3xx is redirect
      int status = conn.getResponseCode();
      if (status != HttpURLConnection.HTTP_OK) {
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
            || status == HttpURLConnection.HTTP_MOVED_PERM
            || status == HttpURLConnection.HTTP_SEE_OTHER)
          redirect = true;
      }

      System.out.println("Response Code ... " + status);

      if (redirect) {

        // get redirect url from "location" header field
        String newUrl = conn.getHeaderField("Location");

        // get the cookie if need, for login
        String cookies = conn.getHeaderField("Set-Cookie");

        // open the new connnection again
        conn = (HttpURLConnection) new URL(newUrl).openConnection();
        conn.setRequestProperty("Cookie", cookies);
        conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
        conn.addRequestProperty("User-Agent", "Mozilla");
        conn.addRequestProperty("Referer", "google.com");

        System.out.println("Redirect to URL : " + newUrl);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public List<URLRedirect> getUrlRedirectList() {
    return urlRedirectList;
  }

  public class URLRedirect {
    private String originalUrl;
    private Integer redirectionCode;
    private String redirectUrl;
    private Integer finalCode;

    public URLRedirect(String originalUrl, Integer redirectionCode, String redirectUrl, Integer finalCode) {
      this.originalUrl = originalUrl;
      this.redirectionCode = redirectionCode;
      this.redirectUrl = redirectUrl;
      this.finalCode = finalCode;
    }

    public String getOriginalUrl() {
      return originalUrl;
    }

    public Integer getRedirectionCode() {
      return redirectionCode;
    }

    public String getRedirectUrl() {
      return redirectUrl;
    }

    public Integer getFinalCode() {
      return finalCode;
    }
  }
}