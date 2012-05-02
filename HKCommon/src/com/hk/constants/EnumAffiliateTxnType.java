package com.hk.constants;

public enum EnumAffiliateTxnType {
  ADD(10L, "Added"),
  SENT(20L, "Sent"),
  ORDER_CANCELLED(30L, "Order Cancelled"),;

  private java.lang.String name;
  private java.lang.Long id;

  EnumAffiliateTxnType(Long id, java.lang.String name) {
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
