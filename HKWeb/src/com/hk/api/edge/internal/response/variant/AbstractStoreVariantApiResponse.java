package com.hk.api.edge.internal.response.variant;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.api.edge.constants.DtoJsonConstants;
import com.hk.api.edge.ext.response.AbstractApiBaseResponse;
import com.hk.api.edge.helper.HKLinkManager;

public abstract class AbstractStoreVariantApiResponse extends AbstractApiBaseResponse {

    // basic
    private Long           id;
    protected Long         storeProductId;
    @JsonProperty(DtoJsonConstants.CAT_PREFIX)
    protected String       catPrefix;
    protected String       categoryName;
    protected String       brandName;

    @JsonProperty(DtoJsonConstants.NAME)
    protected String       name;
    @JsonProperty(DtoJsonConstants.OFFER_PRICE)
    protected int          offerPrice;

    @JsonProperty(DtoJsonConstants.MRP)
    protected Double       mrp;
    @JsonProperty(DtoJsonConstants.DISCOUNT)
    protected Double       discount;

    @JsonProperty(DtoJsonConstants.RANK)
    protected double       rank;
    @JsonProperty(DtoJsonConstants.RATING)
    protected double       rating;

    @JsonProperty(DtoJsonConstants.PRIMARY_IMAGE)
    protected VariantImage primaryImage;

    @JsonProperty(DtoJsonConstants.NAV_KEY)
    protected String       navKey;
    @JsonProperty(DtoJsonConstants.URL_FRAGMENT)
    protected String       urlFragment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
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

    public VariantImage getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(VariantImage primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Long getStoreProductId() {
        return storeProductId;
    }

    public void setStoreProductId(Long storeProductId) {
        this.storeProductId = storeProductId;
    }

    public String getCatPrefix() {
        return catPrefix;
    }

    public void setCatPrefix(String catPrefix) {
        this.catPrefix = catPrefix;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return HKLinkManager.getVariantUrl(this);
    }

    public String getNavKey() {
        return navKey;
    }

    public void setNavKey(String navKey) {
        this.navKey = navKey;
    }

    public String getUrlFragment() {
        return urlFragment;
    }

    public void setUrlFragment(String urlFragment) {
        this.urlFragment = urlFragment;
    }

    /*
     * @Override protected List<String> getKeys() { List<String> keyList = super.getKeys();
     * keyList.add(DtoJsonConstants.ID); keyList.add(DtoJsonConstants.NAME); keyList.add(DtoJsonConstants.OFFER_PRICE);
     * keyList.add(DtoJsonConstants.CAT_PREFIX); keyList.add(DtoJsonConstants.MRP);
     * keyList.add(DtoJsonConstants.DISCOUNT); keyList.add(DtoJsonConstants.RANK); keyList.add(DtoJsonConstants.RATING);
     * keyList.add(DtoJsonConstants.PRIMARY_IMAGE); keyList.add(DtoJsonConstants.URL); return keyList; } @Override
     * protected List<Object> getValues() { double discount = 0; if (mrp != 0) { discount = (mrp - offerPrice) / mrp *
     * 100; } List<Object> valueList = super.getValues(); valueList.add(this.id); valueList.add(this.name);
     * valueList.add(Math.floor(this.offerPrice)); valueList.add(this.catPrefix); valueList.add(this.mrp);
     * valueList.add(Math.floor(discount)); valueList.add(this.rank); valueList.add(this.rating);
     * valueList.add(this.primaryImage); valueList.add(getUrl()); return valueList; }
     */
}
