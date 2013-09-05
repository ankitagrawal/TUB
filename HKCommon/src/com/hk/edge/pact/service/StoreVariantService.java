package com.hk.edge.pact.service;

import com.hk.api.edge.internal.response.variant.StoreVariantBasicApiResponse;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface StoreVariantService {

    
    public StoreVariantBasicApiResponse getStoreVariantBasicDetails(String oldVariantId);
}
