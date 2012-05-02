package com.hk.constants.discount;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: May 30, 2011
 * Time: 4:31:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumRewardPointStatus {

  PENDING(10L, "Pending"),
  APPROVED(20L, "Approved"),
  EXPIRED(30L, "Expired"),
  CANCELLED(40L, "Cancelled");

  private java.lang.String name;
  private java.lang.Long id;

  EnumRewardPointStatus(Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

}
