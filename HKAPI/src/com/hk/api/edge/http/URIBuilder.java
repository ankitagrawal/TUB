package com.hk.api.edge.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.api.edge.constants.EnvConstants;
import com.hk.api.edge.constants.RequestConstants;
import com.hk.api.edge.constants.StoreConstants;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class URIBuilder {

    private static Logger logger = LoggerFactory.getLogger(URIBuilder.class);

    public static final String URL_PARAM_SEP = "&";
    public static final String URL_TOKEN_SEP = "/";
    public static final String URL_FIRST_PARAM_SEP = "?";
    public static final String URL_PARAM_VAL_SEP = "=";

    private static final String DEFAULT_PARAM = "z.ts=";
    private static final String DEFAULT_ENCODING_SCHEME = "UTF-8";

    private Map<String, String> parameters = new HashMap<String, String>();
    private String serviceUrl;
    private boolean isSecure;


    public URIBuilder() {

    }


    private URIBuilder(String serviceUrl, boolean secure) {
      this.serviceUrl = serviceUrl;
      isSecure = secure;
    }

    public URIBuilder fromURI(String uri) {
      return fromURI(uri, false);
    }

    public URIBuilder fromURI(String uri, boolean isSecure) {
      return fromURI(EnvConstants.API_SERVER_URL, uri, isSecure);
    }


    public URIBuilder fromURI(String baseUriIdentifier, String uri) {
      return fromURI(baseUriIdentifier, uri, false);
    }


    public URIBuilder fromURI(String baseUriIdentifier, String uri, boolean isSecure) {
      String serviceUrl;
      if (isSecure) {
        serviceUrl = URIBuilderHelper.getSecureUrlByBaseUriIdentifier(baseUriIdentifier).concat(uri);
      } else {
        serviceUrl = URIBuilderHelper.getUrlByBaseUriIdentifier(baseUriIdentifier).concat(uri);
      }

      return new URIBuilder(serviceUrl, isSecure);
    }


    /*public URIBuilder fromHKPayURI(String uri, boolean isSecure) {
      String serviceUrl = null;
      if (isSecure) {
        serviceUrl = URIBuilderHelper.getHKPaySecureURL().concat(uri);
      } else {
        serviceUrl = URIBuilderHelper.getHKPayURL().concat(uri);
      }

      return new URIBuilder(serviceUrl, isSecure);
    }

    public URIBuilder fromHKPayURI(String uri) {
      return fromHKPayURI(uri, false);
    }*/

    public URIBuilder queryParameter(String paramName, String paramValue) {
      if (StringUtils.isNotBlank(paramValue)) {
        try {
          String encodedValue = URLEncoder.encode(paramValue, DEFAULT_ENCODING_SCHEME);
          parameters.put(paramName, encodedValue);
        } catch (UnsupportedEncodingException uee) {
          logger.error("Could not encode parameter" + uee);
        }
      }
      return this;
    }

    public URIBuilder queryParameters(Map<String, String> params) {
      parameters.putAll(params);
      return this;
    }


    public String getWebServiceUrl() {

      StringBuilder webServiceUrl = new StringBuilder(this.serviceUrl);
      webServiceUrl.append(URL_FIRST_PARAM_SEP).append(DEFAULT_PARAM).append(URL_PARAM_VAL_SEP).append(System.currentTimeMillis());

      for (Map.Entry<String, String> paramEntry : parameters.entrySet()) {
        webServiceUrl.append(URL_PARAM_SEP).append(paramEntry.getKey()).append(URL_PARAM_VAL_SEP).append(paramEntry.getValue());
      }

      webServiceUrl.append(URL_PARAM_SEP).append(RequestConstants.STORE_ID).append(URL_PARAM_VAL_SEP).append(StoreConstants.DEFAULT_STORE_ID);


      return webServiceUrl.toString();
    }

    public String getHKPayWebServiceUrl() {
      return this.serviceUrl;
    }

    public Map<String, String> getParameters() {
      return parameters;
    }


    public boolean isSecure() {
      return isSecure;
    }
}
