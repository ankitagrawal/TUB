package com.hk.api.edge.integration.impl.service.variant;

import org.springframework.stereotype.Service;

import com.hk.api.edge.constants.ServiceEndPoints;
import com.hk.api.edge.http.HkHttpClient;
import com.hk.api.edge.http.URIBuilder;
import com.hk.edge.pact.service.HybridStoreVariantService;
import com.hk.edge.response.variant.StoreVariantBasicResponse;
import com.hk.edge.response.variant.StoreVariantBasicResponseWrapper;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HybridStoreVariantServiceImpl implements HybridStoreVariantService {

    private static final String BASIC_STORE_VARIANT_SUFFIX = "oldVariant/";

    @Override
    public StoreVariantBasicResponse getStoreVariantBasicDetailsFromEdge(String oldVariantId) {
        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.STORE_VARIANT + BASIC_STORE_VARIANT_SUFFIX + oldVariantId);
        StoreVariantBasicResponseWrapper variantBasicResponseWrapper = (StoreVariantBasicResponseWrapper) HkHttpClient.executeGet(builder.getWebServiceUrl(),
                StoreVariantBasicResponseWrapper.class);
        if (variantBasicResponseWrapper != null) {
            return variantBasicResponseWrapper.getStoreVariantBasic();
        }

        return null;
    }

}
