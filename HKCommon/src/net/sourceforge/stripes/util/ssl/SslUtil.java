package net.sourceforge.stripes.util.ssl;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.Keys;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Ssl;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.util.HttpUrlInfo;
import net.sourceforge.stripes.util.UrlParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.Properties;

/**
 * SslUtil
 *
 * @author Christian Schwanke
 */
public class SslUtil {

  private static final Log log = LogFactory.getLog(SslUtil.class);

	private static final String ENVIRONMENT_PROPERTIES = "/environment.properties";

  /**
   * Encodes the given URL by adding the session ID in case url rewriting of the session is needed. Since this task is
   * done by the container, this method will simply delegate to the current {@link javax.servlet.http.HttpServletResponse}. Absolute
   * URLs (i.e. URLs not starting with a single slash) will not be rewritten.
   *
   * @param url the URL that should be encoded
   * @return the encoded URL including session id if applicable
   * @see javax.servlet.http.HttpServletResponse
   */
  public static String encodeUrl(HttpServletRequest request, HttpServletResponse response, String url,
                                 boolean redirect, boolean prependContext) {
    String contextPath = request.getContextPath();


	  /**
	   * The header x-proto will be set from haproxy
	   * The communication from haproxy to apache to tomcat will happen on http hence we set a header to identify secure browsing.
	   */

	  if (url.startsWith("/")) {

      // Parse the raw URL
      HttpUrlInfo urlInfo = null;
      try {
        urlInfo = UrlParser.parseUrl(url);
      } catch (MalformedURLException e) {
        log.error("Unable to parse Url '" + url + "', therefore SSL rewriting cannot be applied.", e);
      }

      // Delegate to the container in order to rewrite the session if necessary
      if (redirect) {
        url = response.encodeRedirectURL(url);
      } else {
        url = response.encodeURL(url);
      }
      String sessionPart = "";
      try {
        if (UrlParser.parseUrl(url).getSession() != null) {
          sessionPart = ";" + UrlParser.parseUrl(url).getSession();
        }
      } catch (MalformedURLException e) {
        log.error("Unable to retrieve session information from url '" + url + "'.", e);
      }

      if (isSslEnabled() && urlInfo != null) {
        String urlInfoPath = urlInfo.getPath();
        if (urlInfoPath.startsWith(contextPath + "/")) {
          urlInfoPath = urlInfoPath.replaceAll(contextPath, "");
        }
        // Retrieve the destination ActionBean of the url
        Class<? extends ActionBean> clazz = StripesFilter.getConfiguration().getActionResolver()
            .getActionBeanType(urlInfoPath);

        if (clazz != null && clazz.isAnnotationPresent(Ssl.class) != WebContext.isSecure()) {
          boolean targetSecure = clazz.isAnnotationPresent(Ssl.class);
          log
              .debug("Current protection state and destination protection state differ - therefore, SSL rewriting is applied");
          String protocol = request.getScheme() + "://";
          String host = request.getServerName();
          if (request.getServerPort() != HttpUrlInfo.DEFAULT_HTTP_PORT
              && request.getServerPort() != HttpUrlInfo.DEFAULT_HTTPS_PORT) {
            host += ":" + request.getServerPort();
          }

          log.debug("default values: " + protocol + host);

          if (targetSecure && !WebContext.isSecure()) {
            log.debug("Rewriting url from http to https");
            protocol = "https://";
            String sslHost = StripesFilter.getConfiguration().getSslConfiguration().getSecureHost();
            if (sslHost != null) {
              host = sslHost;
            }
          } else if (!targetSecure && WebContext.isSecure()) {
            log.debug("Rewriting url from https to http");
            protocol = "http://";
            String unsecureHost = StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost();
            if (unsecureHost != null) {
              host = unsecureHost;
            }
          }

          StringBuffer urlBuffer = new StringBuffer();
          urlBuffer.append(protocol);
          urlBuffer.append(host);

          // Append the context path, but only if the user didn't already
          if (!"/".equals(contextPath) && !url.contains(contextPath + "/")) {
            urlBuffer.append(contextPath);
          }
          urlBuffer.append(urlInfo.getPath());

          String sessionMode = getSessionMode();
          if (sessionMode.equals("auto")) {
            urlBuffer.append(";").append(sessionPart);
          } else if (sessionMode.equals("always")) {
            urlBuffer.append(";jsessionid=").append(request.getSession().getId());
          }

          urlBuffer.append(urlInfo.getCompleteQuery());
          urlBuffer.append(urlInfo.getCompleteRef());
          url = urlBuffer.toString();
        } else {
          url = addContextPath(url, contextPath, prependContext);
        }
      } else {
        url = addContextPath(url, contextPath, prependContext);
      }
    } else {
      // if the url does not start with a /
      if (redirect) {
        url = response.encodeRedirectURL(url);
      } else {
        url = response.encodeURL(url);
      }
    }
    // if url ends with a semicolon, strip it
    if (url.charAt(url.length() - 1) == ';') {
      url = url.substring(0, url.length() - 1);
    }

    log.debug("Final url is: " + url);
    return url;
  }

  /**
   * this method is different from the one above as it *always* prepends the url with the protocol, port and context path
   * <p/>
   *
   * @param request
   * @param response
   * @param url the URL that should be encoded
   * @param contextPath
   * @return the encoded URL including session id if applicable
   * @see javax.servlet.http.HttpServletResponse
   */
  public static String encodeUrlFullForced(HttpServletRequest request, HttpServletResponse response, String url, String contextPath) {

	  String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + ENVIRONMENT_PROPERTIES;
		Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
	  String projectEnv = (String)properties.get(Keys.Env.projectEnv) ;

    contextPath = request != null ? request.getContextPath() : contextPath;

    if (url.startsWith("/")) {

      // Parse the raw URL
      HttpUrlInfo urlInfo = null;
      try {
        urlInfo = UrlParser.parseUrl(url);
      } catch (MalformedURLException e) {
        log.error("Unable to parse Url '" + url + "', therefore SSL rewriting cannot be applied.", e);
      }

      // Delegate to the container in order to rewrite the session if necessary
      url = response != null ? response.encodeURL(url) : url;
      String sessionPart = "";
      try {
        if (UrlParser.parseUrl(url).getSession() != null) {
          sessionPart = ";" + UrlParser.parseUrl(url).getSession();
        }
      } catch (MalformedURLException e) {
        log.error("Unable to retrieve session information from url '" + url + "'.", e);
      }

      if (urlInfo != null) {
        String urlInfoPath = urlInfo.getPath();
        if (urlInfoPath.startsWith(contextPath + "/")) {
          urlInfoPath = urlInfoPath.replaceAll(contextPath, "");
        }
        // Retrieve the destination ActionBean of the url
        Class<? extends ActionBean> clazz = StripesFilter.getConfiguration().getActionResolver()
            .getActionBeanType(urlInfoPath);

        boolean targetSecure = clazz.isAnnotationPresent(Ssl.class);
        log.debug("Current protection state and destination protection state differ - therefore, SSL rewriting is applied");

        StringBuffer urlBuffer = new StringBuffer();
        if (request != null) {
            String protocol = request.getScheme() + "://";
            String host = request.getServerName();
            if (request.getServerPort() != HttpUrlInfo.DEFAULT_HTTP_PORT
                && request.getServerPort() != HttpUrlInfo.DEFAULT_HTTPS_PORT) {
              host += ":" + request.getServerPort();
            }

            log.debug("default values: protocol:host:projectEnv: " + protocol + ":" + host + ":" + projectEnv);

	          //If the env is not prod or admin the url will be formed via the request's servername and port.
	          if(projectEnv.equals("prod") || projectEnv.equals("admin")) {
		          if (targetSecure && isSslEnabled()) {
			          log.debug("Rewriting url from http to https");
			          protocol = "https://";
			          String sslHost = StripesFilter.getConfiguration().getSslConfiguration().getSecureHost();
			          if (sslHost != null) {
				          host = sslHost;
			          }
		          } else {
			          log.debug("Rewriting url from https to http");
			          protocol = "http://";
			          String unsecureHost = StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost();
			          if (unsecureHost != null) {
				          host = unsecureHost;
			          }
		          }
	          }
            urlBuffer.append(protocol);
            urlBuffer.append(host);
        } else {
          String protocol = "";
          String host = "";
          if (targetSecure && isSslEnabled()) {
            log.debug("Rewriting url from http to https");
            protocol = "https://";
            String sslHost = StripesFilter.getConfiguration().getSslConfiguration().getSecureHost();
            if (sslHost != null) {
              host = sslHost;
            }
          } else {
            log.debug("Rewriting url from https to http");
            protocol = "http://";
            String unsecureHost = StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost();
            if (unsecureHost != null) {
              host = unsecureHost;
            }
          }
          urlBuffer.append(protocol);
          urlBuffer.append(host);
        }


        // Append the context path, but only if the user didn't already
        if (!"/".equals(contextPath) && !url.contains(contextPath + "/")) {
          urlBuffer.append(contextPath);
        }
        urlBuffer.append(urlInfo.getPath());

        String sessionMode = getSessionMode();
        if (sessionMode.equals("auto")) {
          if (sessionPart != null && !sessionPart.trim().equals("")) {
              urlBuffer.append(";").append(sessionPart);
          }
        } else if (sessionMode.equals("always")) {
          if (request != null) {
              urlBuffer.append(";jsessionid=").append(request.getSession().getId());
          }
        }

        urlBuffer.append(urlInfo.getCompleteQuery());
        urlBuffer.append(urlInfo.getCompleteRef());
        url = urlBuffer.toString();
      } else {
        url = addContextPath(url, contextPath, true);
      }
    } else {
      // if the url does not start with a /
      url = response != null ? response.encodeURL(url) : url;
    }
    // if url ends with a semicolon, strip it
    if (url.charAt(url.length() - 1) == ';') {
      url = url.substring(0, url.length() - 1);
    }

    log.debug("Final url is: " + url);
    return url;
  }

  protected static String addContextPath(String url, String contextPath, boolean prependContext) {
    // Append the context path, but only if the user didn't already
    if (prependContext && !"/".equals(contextPath) && !url.contains(contextPath + "/")) {
      url = contextPath + url;
    }
    return url;
  }

  protected static boolean isSslEnabled() {
    return StripesFilter.getConfiguration().getSslConfiguration().isSslEnabled();
  }

  /**
   * Retrieves the {@literal SSL.SessionMode} value from the configuration. If no value is found, {@literal never} is
   * returned as default value since this is most secure option.
   *
   * @return The sessionMode
   */
  protected static String getSessionMode() {
    String sessionMode = StripesFilter.getConfiguration().getSslConfiguration().getSessionMode();
    if (sessionMode == null || sessionMode.length() == 0) {
      return "never";
    }
    return sessionMode;
  }

}
