package com.hk.constants.order;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit
 * Date: 11/25/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumBookingType {

  SINGLE_BOOKED(10L, "Single Booked"),
  PARTIAL_BOOKED(20L, "partial Booked"),
  COMPLETE_BOOKED(30L, "complete Booked");
  private java.lang.String name;

  private java.lang.Long id;

  EnumBookingType(Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public EnumBookingType EnumBookingType(Long id) {
    for (EnumBookingType enumBookingType : EnumBookingType.values()) {
      if (enumBookingType.getId().equals(id))
        return enumBookingType;
    }
    return null;
  }

  public static List<EnumBookingType> getAllOrderBookingType() {
    return Arrays.asList(EnumBookingType.SINGLE_BOOKED, EnumBookingType.PARTIAL_BOOKED, EnumBookingType.COMPLETE_BOOKED);
  }

}
