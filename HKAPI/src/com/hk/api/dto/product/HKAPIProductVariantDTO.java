package com.hk.api.dto.product;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 */
public class HKAPIProductVariantDTO {
    private String productVariantID;
    private boolean outOfStock;
    private boolean deleted;
    private double mrp;
    private double hkPrice;
    private double hkDiscountPercent;
    private HKAPIProductOptionDTO[] productOptions;

    public String getProductVariantID() {
        return productVariantID;
    }

    public void setProductVariantID(String productVariantID) {
        this.productVariantID = productVariantID;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getHkPrice() {
        return hkPrice;
    }

    public void setHkPrice(double hkPrice) {
        this.hkPrice = hkPrice;
    }

    public double getHkDiscountPercent() {
        return hkDiscountPercent;
    }

    public void setHkDiscountPercent(double hkDiscountPercent) {
        this.hkDiscountPercent = hkDiscountPercent;
    }

    public HKAPIProductOptionDTO[] getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(HKAPIProductOptionDTO[] productOptions) {
        this.productOptions = productOptions;
    }
}
