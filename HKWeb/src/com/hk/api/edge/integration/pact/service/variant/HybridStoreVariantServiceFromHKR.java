package com.hk.api.edge.integration.pact.service.variant;

import com.hk.api.edge.integration.response.variant.FreeVariantResponseFromHKR;

/**
 * @author Rimal
 */
public interface HybridStoreVariantServiceFromHKR {

    public FreeVariantResponseFromHKR getFreeVariantForProductVariant(String productVariantId);
}
