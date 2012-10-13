package com.hk.rest.mobile.service.model;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 26, 2012
 * Time: 5:03:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MCatalogJSONResponse {

    private String manufacturer;
    private String id;
    private String name;
    private String thumbUrl;
    private Double orderRanking;
    private String brand;
    private String overview;
    private String description;
    private String productUrl;
    private String productSlug;
    private String currentCategory;

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public void setProductSlug(String productSlug) {
        this.productSlug = productSlug;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;
    private Long minDays;
    private Long maxDays;

    private boolean deleted;
    private boolean isProductHaveColorOptions;
    private boolean isService;


    public Double getMarkedPrice() {
        return markedPrice;
    }

    public Double getHkPrice() {
        return hkPrice;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setMarkedPrice(Double markedPrice) {
        this.markedPrice = markedPrice;
    }

    public void setHkPrice(Double hkPrice) {
        this.hkPrice = hkPrice;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    private boolean isGoogleAdDisallowed;
    private boolean isJit;
    private boolean outOfStock;
    private boolean dropShipping;
    private boolean codAllowed;
    private String productURL;
    private boolean hasMore;

    private Double markedPrice;
    private Double hkPrice;
    private Double discountPercentage;



    public String getManufacturer() {
        return manufacturer;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public Double getOrderRanking() {
        return orderRanking;
    }

    public String getBrand() {
        return brand;
    }

    public String getOverview() {
        return overview;
    }

    public String getDescription() {
        return description;
    }

    public Long getMinDays() {
        return minDays;
    }

    public Long getMaxDays() {
        return maxDays;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isProductHaveColorOptions() {
        return isProductHaveColorOptions;
    }

    public boolean isService() {
        return isService;
    }

    public boolean isGoogleAdDisallowed() {
        return isGoogleAdDisallowed;
    }

    public boolean isJit() {
        return isJit;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public boolean getDropShipping() {
        return dropShipping;
    }

    public boolean isCodAllowed() {
        return codAllowed;
    }

    public String getProductURL() {
        return productURL;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setOrderRanking(Double orderRanking) {
        this.orderRanking = orderRanking;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinDays(Long minDays) {
        this.minDays = minDays;
    }

    public void setMaxDays(Long maxDays) {
        this.maxDays = maxDays;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setProductHaveColorOptions(boolean productHaveColorOptions) {
        isProductHaveColorOptions = productHaveColorOptions;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public void setGoogleAdDisallowed(boolean googleAdDisallowed) {
        isGoogleAdDisallowed = googleAdDisallowed;
    }

    public void setJit(boolean jit) {
        isJit = jit;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public void setDropShipping(boolean dropShipping) {
        this.dropShipping = dropShipping;
    }

    public void setCodAllowed(boolean codAllowed) {
        this.codAllowed = codAllowed;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
