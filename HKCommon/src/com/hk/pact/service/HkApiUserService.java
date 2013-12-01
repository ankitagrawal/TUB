package com.hk.pact.service;

import com.hk.domain.api.HkApiUser;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HkApiUserService {
    
    public HkApiUser getApiUserByName(String name);
    
    
    public HkApiUser getApiUserByApiKey(String apiKey);

}
