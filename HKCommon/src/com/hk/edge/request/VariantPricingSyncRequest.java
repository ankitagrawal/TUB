package com.hk.edge.request;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class VariantPricingSyncRequest {

    protected String oldVariantId;
    protected int    offerPrice;
    protected double discount;

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

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(oldVariantId).append(" offer price: ").append(offerPrice).append(" discount : ").append(discount);
        
        return strBuilder.toString();
    
    }
    
    
    

}
