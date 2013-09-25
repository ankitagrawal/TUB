package com.hk.api.edge.integration.response.variant;

import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

/**
 * @author Rimal
 */
public class FreeVariantResponseFromHKR extends AbstractResponseFromHKR {

    private String variantId;
    private String freeVariantId;


    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getFreeVariantId() {
        return freeVariantId;
    }

    public void setFreeVariantId(String freeVariantId) {
        this.freeVariantId = freeVariantId;
    }


    @Override
    protected String[] getKeys() {
        return new String[]{"oldVarId", "freeVarOldVarId", "exception", "msgs"};
    }

    @Override
    protected Object[] getValues() {
        return new Object[]{this.variantId, this.freeVariantId, this.exception, this.msgs};
    }
}
