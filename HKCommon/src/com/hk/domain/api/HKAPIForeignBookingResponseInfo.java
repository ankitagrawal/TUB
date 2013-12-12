package com.hk.domain.api;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit
 * Date: 8/27/13
 * Time: 12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class HKAPIForeignBookingResponseInfo {
  private Long fsiCLIId;
  private Long fsgId;
  private Long fsiId;
  private String barcode;
  private String batch;
  private Double mrp;
  private Double cp;
  private String mfgdt;
  private String expdt;
  private String processed;
  private Long fsoId;
  private Long fboId;
  private Long unitNum;
  private Long whId;

  private String fsogwId;
  public String getFsogwId(){
      return fsogwId;
  }

  public void setFsogwId(String fsogwId){
      this.fsogwId = fsogwId;
  }



  public Long getFsiCLIId() {
    return fsiCLIId;
  }

  public void setFsiCLIId(Long fsiCLIId) {
    this.fsiCLIId = fsiCLIId;
  }

  public Long getFsgId() {
    return fsgId;
  }

  public void setFsgId(Long fsgId) {
    this.fsgId = fsgId;
  }

  public Long getFsiId() {
    return fsiId;
  }

  public void setFsiId(Long fsiId) {
    this.fsiId = fsiId;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getBatch() {
    return batch;
  }

  public void setBatch(String batch) {
    this.batch = batch;
  }

  public Double getMrp() {
    return mrp;
  }

  public void setMrp(Double mrp) {
    this.mrp = mrp;
  }

  public Double getCp() {
    return cp;
  }

  public void setCp(Double cp) {
    this.cp = cp;
  }

  public String getMfgdt() {
    return mfgdt;
  }

  public void setMfgdt(String mfgdt) {
    this.mfgdt = mfgdt;
  }

  public String getExpdt() {
    return expdt;
  }

  public void setExpdt(String expdt) {
    this.expdt = expdt;
  }

  public String getProcessed() {
    return processed;
  }

  public void setProcessed(String processed) {
    this.processed = processed;
  }

  public Long getFsoId() {
    return fsoId;
  }

  public void setFsoId(Long fsoId) {
    this.fsoId = fsoId;
  }

  public Long getFboId() {
    return fboId;
  }

  public void setFboId(Long fboId) {
    this.fboId = fboId;
  }

  public Long getUnitNum() {
    return unitNum;
  }

  public void setUnitNum(Long unitNum) {
    this.unitNum = unitNum;
  }

  public Long getWhId() {
    return whId;
  }

  public void setWhId(Long whId) {
    this.whId = whId;
  }
}
