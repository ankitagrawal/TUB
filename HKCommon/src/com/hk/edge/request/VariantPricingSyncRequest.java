package com.hk.edge.request;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class VariantPricingSyncRequest {

    private String oldVariantId;
    private int    offerPrice;
    private double discount;

    public String getOldVariantId() {
        return oldVariantId;
    }

    public void setOldVariantId(String oldVariantId) {
        this.oldVariantId = oldVariantId;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

}
