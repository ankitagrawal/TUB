package com.hk.api.dto.product;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 */
public class HKAPIProductDTO {
    private String productID;
    private String imageUrl;
    private String name;
    private String overview;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
