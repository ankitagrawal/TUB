package com.hk.api;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface AuthAPI extends HkAPI{

    
    /**
     * currently we will handle only username/pwd auth scheme, will support something else later.
     * @param authToken
     * @param apiKey
     * @param authScheme
     * @return
     */
    public String validateAndRefreshAuthToken(String authToken, String apiKey, String authScheme);
    
}
