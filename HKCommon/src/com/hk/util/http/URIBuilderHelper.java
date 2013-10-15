package com.hk.util.http;

import com.hk.constants.edge.EnvConstants;
import com.hk.pact.service.LoadPropertyService;
import com.hk.service.ServiceLocatorFactory;

public class URIBuilderHelper {

    private static final String UNSECURE_URL_PREFIX = "http://";
    private static final String SECURE_URL_PREFIX = "https://";
    private static final String DEFAULT_API_SERVER_URL = "dev.healthkart.com";
    

   

    private static LoadPropertyService loadPropertyService;

  
    public static String getUrlByBaseUriIdentifier(String baseUriIdentifier) {
      if (EnvConstants.API_SERVER_URL.equals(baseUriIdentifier)) {
        return getURL(EnvConstants.API_SERVER_URL, DEFAULT_API_SERVER_URL);
      }

      return null;
    }


    public static String getSecureUrlByBaseUriIdentifier(String baseUriIdentifier) {
      if (EnvConstants.API_SERVER_URL.equals(baseUriIdentifier)) {
        return getSecureURL(EnvConstants.API_SERVER_URL, DEFAULT_API_SERVER_URL);
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
