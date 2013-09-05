package com.hk.api.edge.integration.request.variant;

public class AddProductVariantToCartRequest {

    private String oldVariantId;

    public AddProductVariantToCartRequest() {

    }

    public AddProductVariantToCartRequest(String oldVariantId) {

        this.oldVariantId = oldVariantId;

    }

    public String getOldVariantId() {
        return oldVariantId;
    }

    public void setOldVariantId(String oldVariantId) {
        this.oldVariantId = oldVariantId;
    }

}
