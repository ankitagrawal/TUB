package com.hk.constants.courier;

import java.util.Arrays;
import java.util.List;

public enum EnumCourierGroupHasCourier {

  COMMON(
      EnumCourierGroup.COMMON,
      Arrays.asList(
          EnumCourier.DTDC_COD,
          EnumCourier.DTDC_Lite,
          EnumCourier.DTDC_Plus,
          EnumCourier.DTDC_Surface,
          EnumCourier.FirstFLight,
          EnumCourier.FirstFLight_COD,
          EnumCourier.AFLWiz
      )
  ),

  Bluedart(
      EnumCourierGroup.Bluedart,
      Arrays.asList(
          EnumCourier.BlueDart,
          EnumCourier.BlueDart_COD
      )
  ),


  Local(
      EnumCourierGroup.Local,
      Arrays.asList(
          EnumCourier.Chhotu,
          EnumCourier.Delhivery
      )
  ),;

  EnumCourierGroup enumCourierGroup;
  List<EnumCourier> enumCouriers;

  EnumCourierGroupHasCourier(EnumCourierGroup enumCourierGroup, List<EnumCourier> enumCouriers) {
    this.enumCourierGroup = enumCourierGroup;
    this.enumCouriers = enumCouriers;
  }

  public List<EnumCourier> getEnumCouriers() {
    return enumCouriers;
  }

  public void setEnumCouriers(List<EnumCourier> enumCouriers) {
    this.enumCouriers = enumCouriers;
  }
}
