package com.hk.api;

import java.util.HashMap;
import java.util.Map;

import com.hk.api.constants.OperationType;
import com.hk.service.ServiceLocatorFactory;
import com.hk.api.locale.LocaleContextHolder;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class APIRegistry {

    
    private static Map<Integer, Map<String, HkAPI>> apiRegistry = new HashMap<Integer, Map<String, HkAPI>>();
    
    
    
    static {
        Map<String, HkAPI> authAPIVersions = new HashMap<String, HkAPI>();
        AuthAPI authAPI = ServiceLocatorFactory.getService(AuthAPI.class);
        authAPIVersions.put("1.0", authAPI);
        apiRegistry.put(OperationType.Auth, authAPIVersions);
        
        
        Map<String, HkAPI> userAPIVersions = new HashMap<String, HkAPI>();
        UserAPI userAPI = ServiceLocatorFactory.getService(UserAPI.class);
        userAPIVersions.put("1.0", userAPI);
        apiRegistry.put(OperationType.USER, userAPIVersions);
        
        

        /*
         * Map<String, HkAPI> userAPIVersions = new HashMap<String, HkAPI>(); userAPIVersions.put("1.0", new
         * HotelSearchAPIImpl()); apiRegistry.put(OperationType.USER, userAPIVersions);
         */

    }
    
    public static AuthAPI getAuthAPI(){
        String apiVersion = LocaleContextHolder.getApiVersion();
        return (AuthAPI) apiRegistry.get(OperationType.Auth).get(apiVersion);
    }
    
    public static UserAPI getUserAPI(){
        String apiVersion = LocaleContextHolder.getApiVersion();
        return (UserAPI) apiRegistry.get(OperationType.USER).get(apiVersion);
    }
    
}
