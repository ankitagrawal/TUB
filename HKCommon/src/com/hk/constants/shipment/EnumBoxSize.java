package com.hk.constants.shipment;

public enum EnumBoxSize {

  XS(10L, "XS"),
  S(20L, "S"),
  M(30L, "M"),
  M2(35L, "M2"),
  L(40L, "L"),
  L2(45L, "L2"),
  XL(50L, "XL"),
  XXL(60L, "XXL"),
  XXXL(70L, "XXXL"),
  MIGRATE(-1L, "MIGRATE");

  private String name;
  private Long id;

  EnumBoxSize(Long id, String name) {
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