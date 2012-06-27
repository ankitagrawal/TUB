package com.hk.constants.core;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 20, 2012
 * Time: 4:17:17 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumState {
  JAMMU_AND_KASHMIR(1L, "JAMMU & KASHMIR"),
  HIMACHAL_PRADESH(2L, "HIMACHAL PRADESH"),
  PUNJAB(3L, "PUNJAB"),
  CHANDIGARH(4L, "CHANDIGARH"),
  UTTARAKHAND(5L, "UTTARAKHAND"),
  HARYANA(6L, "HARYANA"),
  DELHI(7L, "DELHI"),
  RAJASTHAN(8L, "RAJASTHAN"),
  UTTAR_PRADESH(9L, "UTTAR PRADESH"),
  BIHAR(10L, "BIHAR"),
  SIKKIM(11L, "SIKKIM"),
  ARUNACHAL_PRADESH(12L, "ARUNACHAL PRADESH"),
  NAGALAND(13L, "NAGALAND"),
  MANIPUR(14L, "MANIPUR"),
  MIZORAM(15L, "MIZORAM"),
  TRIPURA(16L, "TRIPURA"),
  MEGHALAYA(17L, "MEGHALAYA"),
  ASSAM(18L, "ASSAM"),
  WEST_BENGAL(19L, "WEST BENGAL"),
  JHARKHAND(20L, "JHARKHAND"),
  ORRISA(21L, "ORRISA"),
  CHHATISGARH(22L, "CHHATISGARH"),
  MADHYA_PRADESH(23L, "MADHYA PRADESH"),
  GUJARAT(24L, "GUJARAT"),
  DAMAN_AND_DIU(25L, "DAMAN AND DIU"),
  DADRA_AND_NAGAR_HAVELI(26L, "DADRA AND NAGAR HAVELI"),
  MAHARASHTRA(27L, "MAHARASHTRA"),
  ANDHRA_PRADESH(28L, "ANDHRA PRADESH"),
  KARNATAKA(29L, "KARNATAKA"),
  GOA(30L, "GOA"),
  LAKSHADWEEP(31L, "LAKSHADWEEP"),
  KERALA(32L, "KERALA"),
  TAMIL_NADU(33L, "TAMIL_NADU"),
  PUDUCHERRY(34L, "PUDUCHERRY"),
  ANDAMAN_AND_NICOBAR_ISLAND(35L, "ANDAMAN AND NICOBAR ISLAND");

  private String name;
  private Long id;

  EnumState(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
