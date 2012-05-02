package com.hk.constants.discount;

/**
 * Generated
 */
public enum EnumRewardPointTxnType {
  ADD(1L, "Added"),
  REDEEM(2L, "Redeem"),
  REFERRED_ORDER_CANCELLED(3L, "Referred Order Cancelled"),
  REFUND(4L, "Refund")
  ;

  private java.lang.String name;
  private java.lang.Long id;

  EnumRewardPointTxnType(Long id, java.lang.String name) {
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

