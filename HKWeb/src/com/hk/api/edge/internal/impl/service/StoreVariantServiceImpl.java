package com.hk.api.edge.internal.impl.service;

import org.springframework.stereotype.Service;

import com.hk.api.edge.constants.ServiceEndPoints;
import com.hk.api.edge.http.HkHttpClient;
import com.hk.api.edge.http.URIBuilder;
import com.hk.edge.pact.service.StoreVariantService;
import com.hk.edge.response.variant.StoreVariantBasicApiResponse;
import com.hk.edge.response.variant.VariantBasicResponseApiWrapper;

/**
 * @author vaibhav.adlakha
 */
@Service
public class StoreVariantServiceImpl implements StoreVariantService {

    private static final String BASIC_STORE_VARIANT_SUFFIX = "oldVariant/";

    @Override
    public StoreVariantBasicApiResponse getStoreVariantBasicDetails(String oldVariantId) {
        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.STORE_VARIANT + BASIC_STORE_VARIANT_SUFFIX + oldVariantId);
        VariantBasicResponseApiWrapper variantBasicResponseWrapper = (VariantBasicResponseApiWrapper) HkHttpClient.executeGet(builder.getWebServiceUrl(),
                VariantBasicResponseApiWrapper.class);
        if (variantBasicResponseWrapper != null) {
            return variantBasicResponseWrapper.getStoreVariantBasic();
        }

        return null;
    }

}
