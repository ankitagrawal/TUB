package com.hk.edge.pact.service;

import com.hk.edge.request.VariantStockSyncRequest;
import com.hk.edge.response.variant.StoreVariantBasicResponse;

/**
 * @author vaibhav.adlakha
 */
public interface HybridStoreVariantService {

    public StoreVariantBasicResponse getStoreVariantBasicDetailsFromEdge(String oldVariantId);

    /**
     * need to call this to update mrp, cp and oos on edge
     * 
     * @param variantStockSyncRequest
     */
    public void syncStockOnEdge(VariantStockSyncRequest variantStockSyncRequest);

    /**
     * this will be called to update details like dispatch days, discount and flags etc when sv is saved on catalog
     * admin
     */
    public void syncVariantDetailsFromEdge();
}
