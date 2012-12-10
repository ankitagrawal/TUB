package com.hk.api.security.interceptor;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import com.hk.api.constants.APITokenTypes;
import com.hk.api.constants.EnumAPIErrorCode;
import com.hk.api.dto.HkAPIBaseDto;
import com.hk.api.locale.LocaleContextHolder;
import com.hk.security.exception.*;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import com.hk.api.HkAPI;
import com.hk.security.HkAuthService;
import com.hk.api.security.annotation.SecureResource;
import com.hk.service.ServiceLocatorFactory;
import org.springframework.stereotype.Component;

@Provider
@Component
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
    public ServerResponse preProcess(HttpRequest httpRequest, ResourceMethod resourceMethod) throws Failure, WebApplicationException {
        SecureResource secureResource=null;
        ServerResponse response=null;
        if(resourceMethod.getMethod().isAnnotationPresent(SecureResource.class)){
            secureResource=resourceMethod.getMethod().getAnnotation(SecureResource.class);
        }else{
            secureResource=resourceMethod.getResourceClass().getAnnotation(SecureResource.class);
        }

        if (secureResource != null) {
            String authToken = getParameter(httpRequest, APITokenTypes.AUTH_TOKEN);
            String apiVersion = getParameter(httpRequest, "apiVersion");
            String apiKey = getParameter(httpRequest, "apiKey");
            String appToken = getHeaderParam(httpRequest, APITokenTypes.APP_TOKEN);
            String userAccessToken = getHeaderParam(httpRequest, APITokenTypes.USER_ACCESS_TOKEN);
            if(secureResource.hasAllTokens() != null){
                for(String tokenType:secureResource.hasAllTokens()){
                    if(tokenType.equals(APITokenTypes.AUTH_TOKEN)){
                      response= validateAuthToken(authToken,apiKey);
                    }else if(tokenType.equals(APITokenTypes.APP_TOKEN)){
                      response = validateAppToken(appToken);
                    }else if(tokenType.equals(APITokenTypes.USER_ACCESS_TOKEN)){
                      response =  validateUserAccessToken(userAccessToken);
                    }
                    if(response!=null){
                        return response;
                    }
                }
            }

           /*to-do we can store api version and user identity in threadlocal once we get the basics working
            if (StringUtils.isEmpty(apiVersion)) {
                apiVersion = HkAPI.CURRENT_VERSION;
            }

            LocaleContextHolder.getLocaleContext().setApiVersion(apiVersion);*/

        }
        return response;
    }

    public ServerResponse validateAppToken(String appToken){
        try{
            if(StringUtils.isEmpty(appToken)){
                return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidAppToken),200,getJsonHeaders());
            }
            getHkAuthService().isValidAppToken(appToken);
        }catch (HkInvalidAppTokenException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidAppToken),200,getJsonHeaders());
        }catch (HkInvalidTokenSignatureException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidTokenSignature),200,getJsonHeaders());
        }catch (HkTokenExpiredException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.TokenExpired),200,getJsonHeaders());
        }catch (HkInvalidApiKeyException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidAppKey),200,getJsonHeaders());
        }
        return null;
    }

    public ServerResponse validateUserAccessToken(String userAccessToken){
        try{
            if(StringUtils.isEmpty(userAccessToken)){
                return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidUserAccessToken),200,getJsonHeaders());
            }
            getHkAuthService().isValidUserAccessToken(userAccessToken);
        }catch (HkInvalidUserAccessTokenException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidUserAccessToken),200,getJsonHeaders());
        }catch (HkInvalidTokenSignatureException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidTokenSignature),200,getJsonHeaders());
        }catch (HkTokenExpiredException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.TokenExpired),200,getJsonHeaders());
        }catch (HkInvalidApiKeyException ex){
            return new ServerResponse(new HkAPIBaseDto(EnumAPIErrorCode.InvalidAppKey),200,getJsonHeaders());
        }
        //to-do use shiro to store this user in security context - this can be use  to sign any message that is sent back
        return null;
    }

    public ServerResponse validateAuthToken(String authToken, String apiKey){
        if (StringUtils.isEmpty(authToken)) {
            return new ServerResponse("NO_AUTH_TOKEN_PASSED", 200, new Headers<Object>());
        }
        /**
         * validate token and api key in auth service, do all in memory
         */
        try {
            getHkAuthService().validateToken(authToken, apiKey, false);
        } catch (Throwable t) {
            return new ServerResponse("INVALID_AUTH_TOKEN", 200, new Headers<Object>());
        }
        return null;
    }

    private String getHeaderParam(HttpRequest request, String paramName){
         List<String> headerValues=request.getHttpHeaders().getRequestHeaders().get(paramName);
        if (headerValues != null && headerValues.size() > 0) {
            return headerValues.get(0);
        }
        return null;
    }

    private Headers<Object> getJsonHeaders(){
        //TODO write proper json headers
        return new Headers<Object>();
    }

    public HkAuthService getHkAuthService() {
        if (hkAuthService == null) {
            hkAuthService = (HkAuthService) ServiceLocatorFactory.getService(HkAuthService.class);
        }
        return hkAuthService;
    }

}
