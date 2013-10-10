package com.shiro;

import com.akube.framework.shiro.Principal;
import com.hk.domain.user.User;

/**
 * Author: Kani Date: Sep 16, 2008
 */
public class PrincipalImpl implements Principal {

  static final long serialVersionUID = 999L;

  private Long id;
  private String name;
  private String email;
  private String userHash;
  private Integer orderCount;
  private String gender;

  private Long assumedId;
  private String assumedName;
  private String assumedEmail;
  private String assumedUserHash;
  private Integer assumedOrderCount;
  private String assumedGender;

  private boolean isAssumed = false;

  public PrincipalImpl(User user) {
    id = user.getId();
    name = user.getName();
    email = user.getEmail();
    userHash = user.getUserHash();
    gender = user.getGender();
    orderCount = user.getOrderCount();
  }

  /**
   * Call this method to assume the identity of the passed assumedUser
   *
   * @param assumedUser
   */
  public void setAssumedIdentity(User assumedUser) {
    this.assumedId = assumedUser.getId();
    this.assumedName = assumedUser.getName();
    this.assumedEmail = assumedUser.getEmail();
    this.assumedUserHash = assumedUser.getUserHash();
    this.assumedGender = assumedUser.getGender();
    this.assumedOrderCount = assumedUser.getOrderCount();
    this.isAssumed = true;
  }

  public void clearAssumedIdentity() {
    this.assumedId = null;
    this.assumedName = null;
    this.assumedEmail = null;
    this.assumedUserHash = null;
    isAssumed = false;
  }

  public Long getId() {
    return assumedId == null ? id : assumedId;
  }

  public String getName() {
    return assumedName == null ? name : assumedName;
  }

  public String getFirstName() {
    return assumedName == null ? name.split(" ")[0] : assumedName.split(" ")[0];
  }

  /*
   * This method is explicitly added for handling the case of changing user name. so that the change can be reflected
   * whithout any delay.
   */
  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return assumedEmail == null ? email : assumedEmail;
  }

  public String getUserHash() {
    return assumedUserHash == null ? userHash : assumedUserHash;
  }

  public boolean isAssumed() {
    return isAssumed;
  }

  public String getGender() {
    return assumedGender==null ? gender : assumedGender;
  }

  public Integer getOrderCount() {
    return assumedOrderCount == null ? orderCount : assumedOrderCount;
  }
}
