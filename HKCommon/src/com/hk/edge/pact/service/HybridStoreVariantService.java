package com.hk.edge.pact.service;

import com.hk.edge.response.variant.StoreVariantBasicResponse;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HybridStoreVariantService {
    
    public StoreVariantBasicResponse getStoreVariantBasicDetailsFromEdge(String oldVariantId);
}
