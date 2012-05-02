package com.hk.constants;



/**
 * Generated
 */
public enum EnumIssuer {

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

  EnumIssuer(String id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  //@Nullable
  public static EnumIssuer getIssuerFromId(String id) {
    for (EnumIssuer issuer : values()) {
      if (issuer.getId().equals(id)) return issuer;
    }
    return null;
  }

}

