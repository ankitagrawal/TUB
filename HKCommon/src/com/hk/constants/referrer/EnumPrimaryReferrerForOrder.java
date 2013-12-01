package com.hk.constants.referrer;


/**
 * Generated
 */
public enum EnumPrimaryReferrerForOrder {

  GOOGLE(1L, "google"),
  FACEBOOK(2L, "facebook"),
  EMAIL(3L, "email"),
  VIZURY(4L, "vizury"),
  HEALTHKART(5L, "healthkart"),
  OTHERS(6L, "other"),
  RFERRAL(7L, "referred"),
  AFFILIATE(8L, "affiliate"),
  OUT_BOUND(9L, "out-bound"),
  NUTRITION_CALLING(10L, "nutrition_calling"),  
  ;

  private java.lang.String name;
  private java.lang.Long id;

  EnumPrimaryReferrerForOrder(java.lang.Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }
}

