package com.hk.domain.api;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 8/14/13
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class HKAPIBookingInfo {

	private Double mrp;
  private Long qty;
  private Long cliId;
  private Long soId;
  private Long boId;
  private Long whId;
  private String pvId;
  private String boDate;
  private Long unitNum;
  private Long fsiCLIId;

  public Double getMrp() {
    return mrp;
  }

  public void setMrp(Double mrp) {
    this.mrp = mrp;
  }

  public Long getQty() {
    return qty;
  }

  public Long getFsiCLIId() {
    return fsiCLIId;
  }

  public void setFsiCLIId(Long fsiCLIId) {
    this.fsiCLIId = fsiCLIId;
  }

  public Long getUnitNum() {
    return unitNum;
  }

  public void setUnitNum(Long unitNum) {
    this.unitNum = unitNum;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public Long getCliId() {
    return cliId;
  }

  public void setCliId(Long cliId) {
    this.cliId = cliId;
  }

  public Long getSoId() {
    return soId;
  }

  public void setSoId(Long soId) {
    this.soId = soId;
  }

  public Long getBoId() {
    return boId;
  }

  public void setBoId(Long boId) {
    this.boId = boId;
  }

  public Long getWhId() {
    return whId;
  }

  public void setWhId(Long whId) {
    this.whId = whId;
  }

  public String getPvId() {
    return pvId;
  }

  public void setPvId(String pvId) {
    this.pvId = pvId;
  }

  public String getBoDate() {
    return boDate;
  }

  public void setBoDate(String boDate) {
    this.boDate = boDate;
  }
}