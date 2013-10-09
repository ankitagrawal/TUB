package com.hk.api.edge.integration.pact.service.variant;

import com.hk.api.edge.integration.response.variant.response.ComboResponseFromHKR;
import com.hk.api.edge.integration.response.variant.response.FreeVariantResponseFromHKR;

/**
 * @author Rimal
 */
public interface HybridStoreVariantServiceFromHKR {

    public FreeVariantResponseFromHKR getFreeVariantForProductVariant(String productVariantId);

    public ComboResponseFromHKR getCombosForProductVariant(String productVariantId, int maxResults);

    public Long getUnbookedInventoryForProductVariant(String productVariantId);
}
