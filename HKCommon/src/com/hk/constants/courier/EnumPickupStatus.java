package com.hk.constants.courier;

import com.hk.domain.courier.PickupStatus;

import java.util.List;
import java.util.Arrays;


public enum EnumPickupStatus {
  OPEN(10L, "Open"),
  CLOSE(20L, "Close");


  private java.lang.String name;
  private java.lang.Long id;

  EnumPickupStatus(java.lang.Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }

  public PickupStatus asPickupStatus() {
    PickupStatus pickupStatus = new PickupStatus();
    pickupStatus.setId(this.getId());
    pickupStatus.setName(this.getName());
    return pickupStatus;
  }

  public static PickupStatus asPickupStatusById(Long pickUpStatusId){
    for(EnumPickupStatus enumPickupStatus : values()){
      if(enumPickupStatus.getId().equals(pickUpStatusId)){
        return enumPickupStatus.asPickupStatus();
      }
    }
    return null;
  }

  public static List<EnumPickupStatus> getPickupStatusList(){
	  return Arrays.asList(EnumPickupStatus.OPEN, EnumPickupStatus.CLOSE);
  }

}
