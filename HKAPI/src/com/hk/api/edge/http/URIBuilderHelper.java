package com.hk.api.edge.http;

public class URIBuilderHelper {

    private static final String UNSECURE_URL_PREFIX = "http://";
    private static final String SECURE_URL_PREFIX = "https://";
    private static final String DEFAULT_API_SERVER_URL = "dev.healthkart.com";
    private static final String DEFAULT_HK_PAY_SERVER_URL = "dev.healthkart.com";

    /*private static String baseApiURL;
    private static String apiURL;
    private static String apiSecureURL;

    private static String baseHKPayURL;
    private static String hkPayURL;
    private static String hkPaySecureURL;*/

    private static LoadPropertyService loadPropertyService;

    /*private static String getApiBaseURL() {
      if (baseApiURL == null) {
        baseApiURL = (String) getLoadPropertyService().getProperty(EnvConstants.API_SERVER_URL, DEFAULT_API_SERVER_URL);
      }
      return baseApiURL;
    }

    public static String getApiURL() {
      if (apiURL == null) {
        apiURL = UNSECURE_URL_PREFIX.concat(getApiBaseURL());
      }
      return apiURL;
    }

    public static String getApiSecureURL() {
      if (apiSecureURL == null) {
        apiSecureURL = SECURE_URL_PREFIX.concat(getApiBaseURL());
      }
      return apiSecureURL;
    }

    private static String getHKPayBaseURL() {
      if (baseHKPayURL == null) {
        baseHKPayURL = (String) getLoadPropertyService().getProperty(EnvConstants.HK_PAY_SERVER_URL, DEFAULT_HK_PAY_SERVER_URL);
      }
      return baseHKPayURL;
    }

    public static String getHKPayURL() {
      if (hkPayURL == null) {
        hkPayURL = UNSECURE_URL_PREFIX.concat(getHKPayBaseURL());
      }
      return hkPayURL;
    }

    public static String getHKPaySecureURL() {
      if (hkPaySecureURL == null) {
        hkPaySecureURL = SECURE_URL_PREFIX.concat(getHKPayBaseURL());
      }
      return hkPaySecureURL;
    }*/


    public static String getUrlByBaseUriIdentifier(String baseUriIdentifier) {
      if (EnvConstants.API_SERVER_URL.equals(baseUriIdentifier)) {
        return getURL(EnvConstants.API_SERVER_URL, DEFAULT_API_SERVER_URL);
      } else if (EnvConstants.HK_PAY_SERVER_URL.equals(baseUriIdentifier)) {
        return getURL(EnvConstants.HK_PAY_SERVER_URL, DEFAULT_HK_PAY_SERVER_URL);
      }

      return null;
    }


    public static String getSecureUrlByBaseUriIdentifier(String baseUriIdentifier) {
      if (EnvConstants.API_SERVER_URL.equals(baseUriIdentifier)) {
        return getSecureURL(EnvConstants.API_SERVER_URL, DEFAULT_API_SERVER_URL);
      } else if (EnvConstants.HK_PAY_SERVER_URL.equals(baseUriIdentifier)) {
        return getSecureURL(EnvConstants.HK_PAY_SERVER_URL, DEFAULT_HK_PAY_SERVER_URL);
      }

      return null;
    }


    private static String getBaseURLByKey(String key, String defaultValue) {
      return (String) getLoadPropertyService().getProperty(key, defaultValue);
    }

    public static String getURL(String key, String defaultValue) {
      return UNSECURE_URL_PREFIX.concat(getBaseURLByKey(key, defaultValue));
    }

    public static String getSecureURL(String key, String defaultValue) {
      return SECURE_URL_PREFIX.concat(getBaseURLByKey(key, defaultValue));
    }


    private static LoadPropertyService getLoadPropertyService() {
      if (loadPropertyService == null) {
        loadPropertyService = ServiceLocatorFactory.getService(LoadPropertyService.class);
      }
      return loadPropertyService;
    }
}
