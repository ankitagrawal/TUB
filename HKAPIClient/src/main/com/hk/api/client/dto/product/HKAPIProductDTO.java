package com.hk.api.client.dto.product;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 */
public class HKAPIProductDTO {
    private String productID;
    private HKAPIProductVariantDTO[] productVariantDTOs;
    private boolean deleted;
    private boolean outOfStock;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public HKAPIProductVariantDTO[] getProductVariantDTOs() {
        return productVariantDTOs;
    }

    public void setProductVariantDTOs(HKAPIProductVariantDTO[] productVariantDTOs) {
        this.productVariantDTOs = productVariantDTOs;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }
}
