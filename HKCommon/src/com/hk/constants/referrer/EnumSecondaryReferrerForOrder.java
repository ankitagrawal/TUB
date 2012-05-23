package com.hk.constants.referrer;


/**
 * Generated
 */
public enum EnumSecondaryReferrerForOrder {
  GOOGLE_PAID(1L, "google_paid"),
  GOOGLE_UNPAID(2L, "google_unpaid"),
  FACEBOOK_PAID(3L, "facebook_paid"),
  FACEBOOK_UNPAID(4L, "facebook_unpaid"),
  FACEBOOK_APP(5L, "facebook_app"),
  AFFILIATE_PAID(6L, "affiliate_paid"),
  AFFILIATE_UNPAID(7L, "affiliate_unpaid"),
  HEALTHKART_HOMEBANNER(8L, "healthkart_homebanner"),
  EMAIL_NOTIFYME(9L, "email_notifyme"),
  AFFILIATE_EXTERNAL(10L, "affiliate_external"),
  AFFILIATE_INTERNAL(11L, "affiliate_internal"),
  ;


  private java.lang.String name;
  private java.lang.Long id;

  EnumSecondaryReferrerForOrder(java.lang.Long id, java.lang.String name) {
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

