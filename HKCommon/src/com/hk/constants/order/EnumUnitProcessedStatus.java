package com.hk.constants.order;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 8/21/13
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumUnitProcessedStatus {

  UNPROCESSED("UNPROCESSED", "UNPROCESSED"),
  PROCESSED("PROCESSED", "PROCESSED"),
  REFUSED("REFUSED", "REFUSED");

  private String id;
  private String name;

  EnumUnitProcessedStatus(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EnumUnitProcessedStatus getEnumUnitProcessedStatus(String Id){
    for (EnumUnitProcessedStatus enumUnitProcessedStatus : EnumUnitProcessedStatus.values()) {
      if(enumUnitProcessedStatus.getId().equals(id)){
        return enumUnitProcessedStatus;
      }
    }
    return null;
  }

}
