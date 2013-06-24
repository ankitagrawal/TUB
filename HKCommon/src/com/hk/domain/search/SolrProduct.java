package com.hk.domain.search;

import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Marut
 * Date: May 30, 2012
 * Time: 6:01:36 PM
 * To change this template use File | Settings | File Templates.
 * This class should be used only for the purpose of indexing products in Solr.
 */
public class SolrProduct {

  @Field
  private
  String id;

  @Field
  private
  List<String> brand;

  @Field
  private
  String name;

  @Field
  private
  String overview;

  @Field
  private
  boolean isGoogleAdDisallowed;

  @Field
  private
  boolean hidden;


  @Field
  private
  double ranking;

  @Field
  private
  long mainImageId;

  @Field
  private
  String description;

  @Field
  private
  double hkPrice;

  @Field
  private
  List<String> category;

  @Field
  private
  List<String> categoryDisplayName;

  @Field
  private
  List<String> variantNames = new ArrayList<String>();

  @Field
  private
  List<String> variantCommaNames = new ArrayList<String>();

  @Field
  private
  String h1;

  @Field
  private
  String title;

  @Field
  private
  String metaKeyword;

  @Field
  private
  String metaDescription;

  @Field
  private
  String descriptionTitle;

  @Field
  private
  String seoDescription;

  @Field
  private
  String keywords;

  @Field
  private
  String slug;

  @Field
  private
  double minimumMRPProducVariantDiscount;

  @Field
  private
  double maximumMRPProducVariantDiscount;

  @Field
  private
  double maximumDiscountProductVariantDiscountPercentage;

  @Field
  private
  double minimumMRPProductVariantMarkedPrice;

  @Field
  private
  double maximumDiscountProductVariantHKPrice;

  @Field
  private
  double maximumDiscountProductVariantMRP;

  @Field
  private
  double maxMRPProductVariantPrice;

  @Field
  private
  double markedPrice;

  @Field
  private
  double postpaidPrice;

  @Field
  private
  boolean deleted;

  @Field
  private
  boolean isCombo;

  @Field
  private
  boolean isService;

  @Field
  private
  boolean outOfStock;

  @Field
  private
  double comboDiscountPercent;

  @Field
  private
  boolean isCODAllowed;

  @Field
  private
  String productUrl;

  @Field
  private
  String smallImageUrl;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getBrand() {
    return brand;
  }

  public void setBrand(List<String> brand) {
    this.brand = brand;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public boolean getGoogleAdDisallowed() {
    return isGoogleAdDisallowed;
  }

  public void setGoogleAdDisallowed(Boolean googleAdDisallowed) {
    isGoogleAdDisallowed = googleAdDisallowed;
  }

  public double getOrderRanking() {
    return ranking;
  }

  public void setOrderRanking(double orderRanking) {
    this.ranking = orderRanking;
  }

  public long getMainImageId() {
    return mainImageId;
  }

  public void setMainImageId(long mainImageId) {
    this.mainImageId = mainImageId;
  }

  public double getHkPrice() {
    return hkPrice;
  }

  public void setHkPrice(double hkPrice) {
    this.hkPrice = hkPrice;
  }

  public List<String> getCategory() {
    return category;
  }

  public void setCategory(List<String> category) {
    this.category = category;
  }

  public List<String> getCategoryDisplayName() {
    return categoryDisplayName;
  }

  public void setCategoryDisplayName(List<String> categoryDisplayName) {
    this.categoryDisplayName = categoryDisplayName;
  }

  public String getH1() {
    return h1;
  }

  public void setH1(String h1) {
    this.h1 = h1;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMetaKeyword() {
    return metaKeyword;
  }

  public void setMetaKeyword(String metaKeyword) {
    this.metaKeyword = metaKeyword;
  }

  public String getMetaDescription() {
    return metaDescription;
  }

  public void setMetaDescription(String metaDescription) {
    this.metaDescription = metaDescription;
  }

  public String getDescriptionTitle() {
    return descriptionTitle;
  }

  public void setDescriptionTitle(String descriptionTitle) {
    this.descriptionTitle = descriptionTitle;
  }

  public String getSeoDescription() {
    return seoDescription;
  }

  public void setSeoDescription(String seoDescription) {
    this.seoDescription = seoDescription;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public double getMaximumDiscountProductVariantHKPrice() {
    return maximumDiscountProductVariantHKPrice;
  }

  public void setMaximumDiscountProductVariantHKPrice(double maximumDiscountProductVariantHKPrice) {
    this.maximumDiscountProductVariantHKPrice = maximumDiscountProductVariantHKPrice;
  }

  public double getMaximumDiscountProductVariantMRP() {
    return maximumDiscountProductVariantMRP;
  }

  public void setMaximumDiscountProductVariantMRP(double maximumDiscountProductVariantMRP) {
    this.maximumDiscountProductVariantMRP = maximumDiscountProductVariantMRP;
  }

  public double getMaxMRPProductVariantPrice() {
    return maxMRPProductVariantPrice;
  }

  public void setMaxMRPProductVariantPrice(double maxMRPProductVariantPrice) {
    this.maxMRPProductVariantPrice = maxMRPProductVariantPrice;
  }

  public double getMarkedPrice() {
    return markedPrice;
  }

  public void setMarkedPrice(double markedPrice) {
    this.markedPrice = markedPrice;
  }

  public double getMinimumMRPProducVariantDiscount() {
    return minimumMRPProducVariantDiscount;
  }

  public void setMinimumMRPProducVariantDiscount(double minimumMRPProducVariantDiscount) {
    this.minimumMRPProducVariantDiscount = minimumMRPProducVariantDiscount;
  }

  public double getMaximumMRPProducVariantDiscount() {
    return maximumMRPProducVariantDiscount;
  }

  public void setMaximumMRPProducVariantDiscount(double maximumMRPProducVariantDiscount) {
    this.maximumMRPProducVariantDiscount = maximumMRPProducVariantDiscount;
  }

  public double getMaximumDiscountProductVariantDiscountPercentage() {
    return maximumDiscountProductVariantDiscountPercentage;
  }

  public void setMaximumDiscountProductVariantDiscountPercentage(double maximumDiscountProductVariantDiscountPercentage) {
    this.maximumDiscountProductVariantDiscountPercentage = maximumDiscountProductVariantDiscountPercentage;
  }

  public double getMinimumMRPProductVariantMarkedPrice() {
    return minimumMRPProductVariantMarkedPrice;
  }

  public void setMinimumMRPProductVariantMarkedPrice(double minimumMRPProductVariantMarkedPrice) {
    this.minimumMRPProductVariantMarkedPrice = minimumMRPProductVariantMarkedPrice;
  }

  public boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public boolean getOutOfStock() {
    return outOfStock;
  }

  public void setOutOfStock(boolean outOfStock) {
    this.outOfStock = outOfStock;
  }

  public double getPostpaidPrice() {
    return postpaidPrice;
  }

  public void setPostpaidPrice(double postpaidPrice) {
    this.postpaidPrice = postpaidPrice;
  }

  public boolean isCODAllowed() {
    return isCODAllowed;
  }

  public void setCODAllowed(boolean CODAllowed) {
    isCODAllowed = CODAllowed;
  }

  public boolean getCombo() {
    return isCombo;
  }

  public void setCombo(boolean combo) {
    isCombo = combo;
  }

  public boolean getService() {
    return isService;
  }

  public void setService(boolean service) {
    isService = service;
  }

  public double getComboDiscountPercent() {
    return comboDiscountPercent;
  }

  public void setComboDiscountPercent(double comboDiscountPercent) {
    this.comboDiscountPercent = comboDiscountPercent;
  }

  public List<String> getVariantNames() {
    return variantNames;
  }

  public void setVariantNames(List<String> variantNames) {
    this.variantNames = variantNames;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public String getSmallImageUrl() {
    return smallImageUrl;
  }

  public void setSmallImageUrl(String smallImageUrl) {
    this.smallImageUrl = smallImageUrl;
  }

  public String getProductUrl() {
    return productUrl;
  }

  public void setProductUrl(String productUrl) {
    this.productUrl = productUrl;
  }

  public List<String> getVariantCommaNames() {
    return variantCommaNames;
  }

  public void setVariantCommaNames(List<String> variantCommaNames) {
    this.variantCommaNames = variantCommaNames;
  }

  public SolrProduct() {

  }

}
