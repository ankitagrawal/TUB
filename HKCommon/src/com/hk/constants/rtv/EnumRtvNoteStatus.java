package com.hk.constants.rtv;

import com.hk.domain.inventory.rtv.RtvNoteStatus;
/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 6:34:49 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumRtvNoteStatus {

  Created(10L, "Created"),
  Reconciled(40L, "Reconciled");

  private String name;
  private Long id;

  EnumRtvNoteStatus(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

   public RtvNoteStatus asRtvNoteStatus() {
    RtvNoteStatus rtvNoteStatus = new RtvNoteStatus();
    rtvNoteStatus.setId(this.getId());
    rtvNoteStatus.setName(this.getName());
    return rtvNoteStatus;
  }
}
