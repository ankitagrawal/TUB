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
  KOMLI(10L, "komli"),
  OHANA(11L, "ohana"),
  OTHERS(12L, "others"),
  NOTIFYME(13L, "notify me"),
  ENEWSLETTER(14L, "e-newsletter"),
  CASHBACK_CALLS(15L, "cashback_calls"),
  RTO_RETRY(16L, "rto_retry"),
  INBOUND_FWD(17L, "inbound_fwd"),
  CALLBACK_REQUEST(18L, "callback_request"),
  EMAIL_REQUEST(19L, "email_request"),
  PAYMENT_FAIL(20L, "payment_fail"),
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

