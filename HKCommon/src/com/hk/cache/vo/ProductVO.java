package com.hk.cache.vo;

import java.io.Serializable;

/**
 * @author ajeet
 */
@SuppressWarnings("serial")
public class ProductVO implements Serializable {


  private String id;

  private String name;

  private boolean isGoogleAdDisallowed;

  private boolean hidden;

  private boolean deleted;

  private boolean outOfStock;

  private double maxDiscountHKPrice;
  
  private double maxDiscountMRP;

  private double maxDiscount;

  private boolean isCombo;

  private boolean isService;

  private String productURL;

  private Long mainImageId;

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

  public boolean isGoogleAdDisallowed() {
    return isGoogleAdDisallowed;
  }

  public void setGoogleAdDisallowed(boolean googleAdDisallowed) {
    isGoogleAdDisallowed = googleAdDisallowed;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public boolean isOutOfStock() {
    return outOfStock;
  }

  public void setOutOfStock(boolean outOfStock) {
    this.outOfStock = outOfStock;
  }

  public double getMaxDiscountHKPrice() {
    return maxDiscountHKPrice;
  }

  public void setMaxDiscountHKPrice(double maxDiscountHKPrice) {
    this.maxDiscountHKPrice = maxDiscountHKPrice;
  }

  public double getMaxDiscountMRP() {
    return maxDiscountMRP;
  }

  public void setMaxDiscountMRP(double maxDiscountMRP) {
    this.maxDiscountMRP = maxDiscountMRP;
  }

  public double getMaxDiscount() {
    return maxDiscount;
  }

  public void setMaxDiscount(double maxDiscount) {
    this.maxDiscount = maxDiscount;
  }

  public boolean isCombo() {
    return isCombo;
  }

  public void setCombo(boolean combo) {
    isCombo = combo;
  }

  public boolean isService() {
    return isService;
  }

  public void setService(boolean service) {
    isService = service;
  }

  public String getProductURL() {
    return productURL;
  }

  public void setProductURL(String productURL) {
    this.productURL = productURL;
  }

  public Long getMainImageId() {
    return mainImageId;
  }

  public void setMainImageId(Long mainImageId) {
    this.mainImageId = mainImageId;
  }
}