package com.hk.api.dto.marketing;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/16/13
 * Time: 12:44 PM
 */
public class HKAPIMarketingProductDTO {

    private String productId;
    private String brand;
    private String primaryCategory;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }
}
