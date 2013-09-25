package com.hk.api.edge.integration.response.variant;

import com.hk.api.edge.integration.response.AbstractResponseFromHKR;

/**
 * @author Rimal
 */
public class FreeVariantResponseFromHKR extends AbstractResponseFromHKR {

    private String variantId;
    private String freebieProductId;
    private String freebieName;


    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getFreebieProductId() {
        return freebieProductId;
    }

    public void setFreebieProductId(String freebieProductId) {
        this.freebieProductId = freebieProductId;
    }

    public String getFreebieName() {
        return freebieName;
    }

    public void setFreebieName(String freebieName) {
        this.freebieName = freebieName;
    }
    

    @Override
    protected String[] getKeys() {
        return new String[]{"oldVarId", "freebieOldProductId", "freebieNm", "exception", "msgs"};
    }

    @Override
    protected Object[] getValues() {
        return new Object[]{this.variantId, this.freebieProductId, this.freebieName, this.exception, this.msgs};
    }
}
