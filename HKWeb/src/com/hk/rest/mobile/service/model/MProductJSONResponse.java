package com.hk.rest.mobile.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hk.rest.mobile.service.utils.MHKConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 5, 2012
 * Time: 5:41:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MProductJSONResponse {

    private String               id="";
    private String               thumbUrl="";
    private Double               orderRanking=0.0;
    private String               brand="";
    private String               packType="";
    private String               overview="";
    private String               description="";
    private String               keywords="";
    private Long                 minDays=0l;
    private Long                 maxDays=0l;
    private Boolean              deleted=false;
    private Long                 mainImageId;
    private Boolean              isProductHaveColorOptions;
    private Boolean              isService;
    private Boolean              isGoogleAdDisallowed;
    private Boolean              hidden;
    private Boolean              isJit;
    private Boolean              isAmazonProduct;
    private Boolean              outOfStock=false;
    private String               supplier;
    private Date                 createDate;
    private String               videoEmbedCode;
    private List<MProductVariantJSONResponse>        relatedProducts  = new ArrayList<MProductVariantJSONResponse>();
    private List<MProductVariantJSONResponse> productVariants  = new ArrayList<MProductVariantJSONResponse>();
    private List<MProductVariantJSONResponse> similarProducts  = new ArrayList<MProductVariantJSONResponse>();
    private String             primaryCategory="";
    private String             secondaryCategory="";
    private boolean              dropShipping;
    private Boolean              codAllowed=false;
    private Boolean              isSubscribable;
    private String               categoriesPipeSeparated;
    private String               productURL="";
    private String               imageUrl="";
    private String name="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = MHKConstants.getStringNullDefault(name);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = MHKConstants.getStringNullDefault(imageUrl);
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = MHKConstants.getStringNullDefault(supplier);
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getVideoEmbedCode() {
        return videoEmbedCode;
    }

    public void setVideoEmbedCode(String videoEmbedCode) {
        this.videoEmbedCode = videoEmbedCode;
    }

    public List<MProductVariantJSONResponse> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<MProductVariantJSONResponse> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public List<MProductVariantJSONResponse> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<MProductVariantJSONResponse> productVariants) {
        this.productVariants = productVariants;
    }

    public List<MProductVariantJSONResponse> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<MProductVariantJSONResponse> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = MHKConstants.getStringNullDefault(primaryCategory);
    }

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = MHKConstants.getStringNullDefault(secondaryCategory);
    }

    public boolean isDropShipping() {
        return dropShipping;
    }

    public void setDropShipping(boolean dropShipping) {
        this.dropShipping = dropShipping;
    }

    public Boolean isCodAllowed() {
        return codAllowed;
    }

    public void setCodAllowed(Boolean codAllowed) {
        this.codAllowed = codAllowed!=null?codAllowed:false;
    }

    public Boolean isSubscribable() {
        return isSubscribable;
    }

    public void setSubscribable(Boolean subscribable) {
        isSubscribable = subscribable!=null?subscribable:false;
    }

    public String getCategoriesPipeSeparated() {
        return categoriesPipeSeparated;
    }

    public void setCategoriesPipeSeparated(String categoriesPipeSeparated) {
        this.categoriesPipeSeparated = categoriesPipeSeparated;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = MHKConstants.getStringNullDefault(productURL);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = MHKConstants.getStringNullDefault(thumbUrl);
    }

    public Double getOrderRanking() {
        return orderRanking;
    }

    public void setOrderRanking(Double orderRanking) {
        this.orderRanking = MHKConstants.getDoubleNullDefault(orderRanking);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = MHKConstants.getStringNullDefault(brand);
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = MHKConstants.getStringNullDefault(overview);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = MHKConstants.getStringNullDefault(description);
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getMinDays() {
        return minDays;
    }

    public void setMinDays(Long minDays) {
        this.minDays = MHKConstants.getLongNullDefault(minDays);
    }

    public Long getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(Long maxDays) {
        this.maxDays = MHKConstants.getLongNullDefault(maxDays);
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(Long mainImageId) {
        this.mainImageId = mainImageId;
    }

    public Boolean isProductHaveColorOptions() {
        return isProductHaveColorOptions;
    }

    public void setProductHaveColorOptions(Boolean productHaveColorOptions) {
        isProductHaveColorOptions = productHaveColorOptions;
    }

    public Boolean isService() {
        return isService;
    }

    public void setService(Boolean service) {
        isService = service;
    }

    public Boolean isGoogleAdDisallowed() {
        return isGoogleAdDisallowed;
    }

    public void setGoogleAdDisallowed(Boolean googleAdDisallowed) {
        isGoogleAdDisallowed = googleAdDisallowed;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean isJit() {
        return isJit;
    }

    public void setJit(Boolean jit) {
        isJit = jit;
    }

    public Boolean isAmazonProduct() {
        return isAmazonProduct;
    }

    public void setAmazonProduct(Boolean amazonProduct) {
        isAmazonProduct = amazonProduct;
    }

    public Boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(Boolean outOfStock) {
        this.outOfStock = outOfStock;
    }
}