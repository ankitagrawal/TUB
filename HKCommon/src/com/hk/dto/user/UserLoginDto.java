package com.hk.dto.user;

import com.hk.domain.user.User;

/**
 * User: kani
 * Time: 19 Feb, 2010 12:13:35 PM
 */
public class UserLoginDto {

  private User loggedUser;
  private User tempUser;
  boolean transferData;

  public UserLoginDto(User loggedUser, User tempUser, boolean transferData) {
    this.loggedUser = loggedUser;
    this.tempUser = tempUser;
    this.transferData = transferData;
  }

  public UserLoginDto(User loggedUser) {
    this.loggedUser = loggedUser;
    tempUser = null;
    transferData = false;
  }

  public User getLoggedUser() {
    return loggedUser;
  }

  public void setLoggedUser(User loggedUser) {
    this.loggedUser = loggedUser;
  }

  public User getTempUser() {
    return tempUser;
  }

  public void setTempUser(User tempUser) {
    this.tempUser = tempUser;
  }

  public boolean isTransferData() {
    return transferData;
  }

  public void setTransferData(boolean transferData) {
    this.transferData = transferData;
  }
}
