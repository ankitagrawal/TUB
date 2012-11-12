package com.hk.security.interceptor;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import com.hk.api.HkAPI;
import com.hk.api.cache.HkApiUserCache;
import com.hk.domain.api.HkApiUser;
import com.hk.security.HkAuthService;
import com.hk.security.annotation.SecureResource;
import com.hk.service.ServiceLocatorFactory;
import com.hk.web.locale.LocaleContextHolder;

@Provider
public class SecureResourceInterceptor implements PreProcessInterceptor {

    private HkAuthService hkAuthService;

    private String getParameter(HttpRequest httpRequest, String key) {
        List<String> values = httpRequest.getUri().getQueryParameters().get(key);
        if (values != null && values.size() > 0) {
            return values.get(0);
        }
        return null;
    }

    @Override
    public ServerResponse preProcess(HttpRequest httpRequest, ResourceMethod method) throws Failure, WebApplicationException {

        SecureResource secureResouce = method.getResourceClass().getAnnotation(SecureResource.class);

        if (secureResouce != null) {
            String authToken = getParameter(httpRequest, "authToken");
            String apiVersion = getParameter(httpRequest, "apiVersion");
            String apiKey = getParameter(httpRequest, "apiKey");

            // check for a not null authToken
            if (StringUtils.isEmpty(authToken)) {
                return new ServerResponse("NO_AUTH_TOKEN_PASSED", 200, new Headers<Object>());
            }

            // check for a not null apiKey
            if (StringUtils.isEmpty(apiKey)) {
                return new ServerResponse("NO_API_KEY_PASSED", 200, new Headers<Object>());
            }

            HkApiUser hkApiUser = HkApiUserCache.getInstance().getHkApiUser(apiKey);
            if (hkApiUser == null) {
                return new ServerResponse("INVALID_APP_KEY", 200, new Headers<Object>());
            }
            

            if (StringUtils.isEmpty(apiVersion)) {
                apiVersion = HkAPI.CURRENT_VERSION;
            }

            LocaleContextHolder.getLocaleContext().setApiVersion(apiVersion);

            /**
             * validate token and api key in auth service, do all in memory
             */
            try {
                getHkAuthService().validateToken(authToken, apiKey, false);
            } catch (Throwable t) {
                return new ServerResponse("INVALID_AUTH_TOKEN", 200, new Headers<Object>());
            }
        }

        return null;
    }

    public HkAuthService getHkAuthService() {
        if (hkAuthService == null) {
            hkAuthService = (HkAuthService) ServiceLocatorFactory.getService(HkAuthService.class);
        }
        return hkAuthService;
    }

}
