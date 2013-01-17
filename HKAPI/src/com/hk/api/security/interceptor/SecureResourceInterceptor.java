package com.hk.api.security.interceptor;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import com.hk.api.constants.EnumHKAPIErrorCode;
import com.hk.api.constants.HKAPITokenTypes;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.security.exception.*;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

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
            String authToken = getParameter(httpRequest, HKAPITokenTypes.AUTH_TOKEN);
            String apiVersion = getParameter(httpRequest, "apiVersion");
            String apiKey = getParameter(httpRequest, "apiKey");
            String appToken = getHeaderParam(httpRequest, HKAPITokenTypes.APP_TOKEN);
            String userAccessToken = getHeaderParam(httpRequest, HKAPITokenTypes.USER_ACCESS_TOKEN);
            if(secureResource.hasAllTokens() != null){
                for(String tokenType:secureResource.hasAllTokens()){
                    if(tokenType.equals(HKAPITokenTypes.AUTH_TOKEN)){
                      response= validateAuthToken(authToken,apiKey);
                    }else if(tokenType.equals(HKAPITokenTypes.APP_TOKEN)){
                      response = validateAppToken(appToken);
                    }else if(tokenType.equals(HKAPITokenTypes.USER_ACCESS_TOKEN)){
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
                return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidAppToken),200,getJsonHeaders());
            }
            getHkAuthService().isValidAppToken(appToken);
        }catch (HkInvalidAppTokenException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidAppToken),200,getJsonHeaders());
        }catch (HkInvalidTokenSignatureException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidTokenSignature),200,getJsonHeaders());
        }catch (HkTokenExpiredException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.TokenExpired),200,getJsonHeaders());
        }catch (HkInvalidApiKeyException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidAppKey),200,getJsonHeaders());
        }
        return null;
    }

    public ServerResponse validateUserAccessToken(String userAccessToken){
        try{
            if(StringUtils.isEmpty(userAccessToken)){
                return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidUserAccessToken),200,getJsonHeaders());
            }
            getHkAuthService().isValidUserAccessToken(userAccessToken);
        }catch (HkInvalidUserAccessTokenException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidUserAccessToken),200,getJsonHeaders());
        }catch (HkInvalidTokenSignatureException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidTokenSignature),200,getJsonHeaders());
        }catch (HkTokenExpiredException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.TokenExpired),200,getJsonHeaders());
        }catch (HkInvalidApiKeyException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.InvalidAppKey),200,getJsonHeaders());
        }catch (HkUserNotFoundException ex){
            return new ServerResponse(new HKAPIBaseDTO(EnumHKAPIErrorCode.UserDoesNotExist),200, getJsonHeaders());
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
