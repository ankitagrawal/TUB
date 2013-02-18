package com.hk.constants.rtv;

import com.hk.domain.inventory.rtv.ExtraInventoryStatus;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Feb 1, 2013
 * Time: 5:46:58 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumExtraInventoryStatus {

  Created(10L, "Created"),
  SentToCategory(20L, "Sent To Category"),
  Closed(40L, "Closed");

  private String name;
  private Long id;

  EnumExtraInventoryStatus(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public static ExtraInventoryStatus  asEnumExtraInventoryStatusByID(Long extraInventoryStatusId){
    for(EnumExtraInventoryStatus extraInventoryStatus : values()){
      if(extraInventoryStatus.getId().equals(extraInventoryStatusId)){
        return extraInventoryStatus.asEnumExtraInventoryStatus();
      }
    }
    return null;
  }

   public ExtraInventoryStatus asEnumExtraInventoryStatus() {
    ExtraInventoryStatus extraInventoryStatus = new ExtraInventoryStatus();
    extraInventoryStatus.setId(this.getId());
    extraInventoryStatus.setName(this.getName());
    return extraInventoryStatus;
  }
}
