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
    private String subCategory1;
    private String subCategory2;
    private String subCategory3;

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

    public String getSubCategory1() {
        return subCategory1;
    }

    public void setSubCategory1(String subCategory1) {
        this.subCategory1 = subCategory1;
    }

    public String getSubCategory2() {
        return subCategory2;
    }

    public void setSubCategory2(String subCategory2) {
        this.subCategory2 = subCategory2;
    }

    public String getSubCategory3() {
        return subCategory3;
    }

    public void setSubCategory3(String subCategory3) {
        this.subCategory3 = subCategory3;
    }
}
