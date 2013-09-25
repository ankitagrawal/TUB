package com.hk.api.edge.integration.impl.service.variant;

import com.hk.api.edge.constants.ServiceEndPoints;
import com.hk.api.edge.http.HkHttpClient;
import com.hk.api.edge.http.URIBuilder;
import com.hk.api.edge.integration.pact.service.variant.HybridStoreVariantServiceFromHKR;
import com.hk.api.edge.integration.response.variant.FreeVariantResponseFromHKR;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.edge.pact.service.HybridStoreVariantService;
import com.hk.edge.response.variant.StoreVariantBasicResponse;
import com.hk.edge.response.variant.StoreVariantBasicResponseWrapper;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.taglibs.Functions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HybridStoreVariantServiceImpl implements HybridStoreVariantService, HybridStoreVariantServiceFromHKR {

    private static final String BASIC_STORE_VARIANT_SUFFIX = "oldVariant/";

    @Autowired
    private ProductVariantService productVariantService;


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

    @Override
    public FreeVariantResponseFromHKR getFreeVariantForProductVariant(String productVariantId) {
        if (StringUtils.isBlank(productVariantId)) {
            throw new InvalidParameterException("PRODUCT_VARIANT_ID_CANNOT_BE_BLANK");
        }

        ProductVariant productVariant = getProductVariantService().getVariantById(productVariantId);

        if (productVariant != null) {
            Product freebie = Functions.showFreebieForVariant(productVariant);
            if (freebie != null) {
                FreeVariantResponseFromHKR freeVariantResponseFromHKR = new FreeVariantResponseFromHKR();

                freeVariantResponseFromHKR.setVariantId(productVariantId);
                freeVariantResponseFromHKR.setFreebieProductId(freebie.getId());
                freeVariantResponseFromHKR.setFreebieName(freebie.getName());

                return freeVariantResponseFromHKR;
            }
        }
        return null;
    }


    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }
}
