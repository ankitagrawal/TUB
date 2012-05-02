package com.hk.constants.inventory;

import com.hk.domain.inventory.rv.ReconciliationType;


/**
 * Generated
 */
public enum EnumReconciliationType {
  Add(10L,"Add"),
  Subtract(20L,"Subtract"),
  Damage(30L,"Damage"),
  Expired(40L,"Expired"),;

  private java.lang.String name;
  private java.lang.Long id;

  EnumReconciliationType(java.lang.Long id,java.lang.String name) {
    this.id = id;
    this.name = name;
  }

  public java.lang.Long getId() {
    return id;
  }
  public java.lang.String getName() {
    return name;
  }


  public ReconciliationType asReconciliationType() {
    ReconciliationType reconciliationType = new ReconciliationType();
    reconciliationType.setId(id);
    reconciliationType.setName(name);
    return reconciliationType;
  }

}

