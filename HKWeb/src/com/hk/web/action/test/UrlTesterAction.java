package com.hk.web.action.test;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.ssl.SslUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ajeet
 * Date: 09 Apr, 2013
 */
@Component
public class UrlTesterAction extends BaseAction {

  @Autowired
  MenuHelper menuHelper;

  String category;
  String server;
  Map<String, Integer> urlResult = new HashMap<String, Integer>();

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
          System.out.println("menuUrl->" + menuUrl);
          URL url = new URL(menuUrl);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setDoOutput(false);
          connection.setDoInput(true);
          connection.setConnectTimeout(1000);
          System.out.println("Opened Con->" + connection);
          connection.connect();

          int code = connection.getResponseCode();
          System.out.println("code ->" + code);
          urlResult.put(menuNodeUrl, code);
          connection.disconnect();
        } catch (IOException e) {
          System.out.println("Unable to connect url: " + e.getMessage());
          e.printStackTrace();
        }
      }
    }
    return new ForwardResolution("/pages/test/urlTester.jsp");
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

  public Map<String, Integer> getUrlResult() {
    return urlResult;
  }
}