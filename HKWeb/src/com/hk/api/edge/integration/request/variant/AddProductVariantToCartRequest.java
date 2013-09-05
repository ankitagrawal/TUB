package com.hk.api.edge.integration.request.variant;

public class AddProductVariantToCartRequest {

    private String oldVariantId;
    private Long   userId;

    public AddProductVariantToCartRequest() {

    }

    public AddProductVariantToCartRequest(String oldVariantId, Long userId) {

        this.oldVariantId = oldVariantId;
        this.userId = userId;

    }

    public String getOldVariantId() {
        return oldVariantId;
    }

    public void setOldVariantId(String oldVariantId) {
        this.oldVariantId = oldVariantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
