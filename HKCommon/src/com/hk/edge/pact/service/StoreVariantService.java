package com.hk.edge.pact.service;

import com.hk.edge.response.variant.StoreVariantBasicApiResponse;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface StoreVariantService {
    
    public StoreVariantBasicApiResponse getStoreVariantBasicDetails(String oldVariantId);
}
