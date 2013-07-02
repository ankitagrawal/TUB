package com.hk.constants.catalog.image;


public enum EnumImageSize {

  Original(null, "o"),
  LargeSize(900, "l"),
  MediumSize(320, "m"),
  BigThumbSize(180, "bt"),
  SmallSize(128, "s"),
  TinySize(48, "t"),
  ;

  private Integer dimension;
  private String suffix;

  EnumImageSize(Integer dimension, String suffix) {
    this.dimension = dimension;
    this.suffix = suffix;
  }

  public Integer getDimension() {
    return dimension;
  }

  public String getSuffix() {
    return suffix;
  }

}
