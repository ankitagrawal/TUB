package com.hk.constants.warehouse;

import java.util.Arrays;
import java.util.List;


public enum EnumWarehouseType {
  Online_B2B(1L, "Online B2B"),
  Online_B2C(2L, "Online B2C"),
  Offline_B2C(3L, "Offline B2C"),;

  private String name;
  private Long id;

  EnumWarehouseType(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public static List<EnumWarehouseType> getAllPaymentTypes() {
    return Arrays.asList(
        EnumWarehouseType.Online_B2B,
        EnumWarehouseType.Online_B2C,
        EnumWarehouseType.Offline_B2C);

  }

}