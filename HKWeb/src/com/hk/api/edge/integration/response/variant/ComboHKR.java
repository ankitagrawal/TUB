package com.hk.api.edge.integration.response.variant;

import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.core.JSONObject;

/**
 * @author Rimal
 */
public class ComboHKR extends JSONObject {

    private String id;
    private String name;
    private String url;
    private String sLinkForImage;
    private String mLinkForImage;

    private Double mrp;
    private Double discount;
    private Double offerPrice;


    public ComboHKR(Combo combo) {
        this.id = combo.getId();
        this.name = combo.getName();
        this.mrp = combo.getMarkedPrice();
        this.discount = combo.getDiscountPercent();
        this.offerPrice = combo.getHkPrice();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSLinkForImage() {
        return sLinkForImage;
    }

    public void setSLinkForImage(String sLinkForImage) {
        this.sLinkForImage = sLinkForImage;
    }

    public String getMLinkForImage() {
        return mLinkForImage;
    }

    public void setMLinkForImage(String mLinkForImage) {
        this.mLinkForImage = mLinkForImage;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        this.offerPrice = offerPrice;
    }

    @Override
    protected String[] getKeys() {
        return new String[]{"id", "nm", "url", "s_link", "m_link", "mrp", "offer_pr", "discount"};
    }

    @Override
    protected Object[] getValues() {
        double discount = 0;
        if (this.mrp != 0) {
            discount = (this.mrp - this.offerPrice) / this.mrp * 100;
        }
        return new Object[]{this.id, this.name, this.url, this.sLinkForImage, this.mLinkForImage, (int) Math.round(this.mrp), (int) Math.round(this.offerPrice), (int) Math.round(discount)};
    }
}