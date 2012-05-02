package com.hk.constants.payment;


/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 1/24/12
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumBankCodes {

  ICICI_Bank("ISS001", "ICICI Bank"),
  AXIS_Bank("ISS002", "AXIS Bank"),
  HDFC_Bank("ISS003", "HDFC Bank"),
  YES_Bank("ISS004", "YES Bank"),
  DEUTSCHE_Bank("ISS006", "DEUTSCHE Bank"),
  UNION_Bank("ISS007", "UNION Bank"),
  SBI_Bank("ISS008", "SBI Bank"),
  ;

  private java.lang.String name;
  private String id;

  EnumBankCodes(String id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public static EnumBankCodes getBankFromId(String id) {
    for (EnumBankCodes bankCode : values()) {
      if (bankCode.getId().equals(id)) return bankCode;
    }
    return null;
  }

}

