package com.hk.constants.discount;

/**
 * Generated
 */
public enum EnumRewardPointMode {
  TPSL_ADJUSTMENTS(10L, "TPSL Adjustments"),
  HK_CASHBACK(20L, "HealthKart Cashback"),
  HK_ADJUSTMENTS(30L, "HealthKart Adjustments"),
  REFERRAL(40L, "Referral"),
  FB_SHARING(50L, "Facebook Sharing");

  private String name;
  private Long id;

  EnumRewardPointMode(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

}