package com.hk.api.rest.models.user;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 2, 2012
 * Time: 4:52:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIUser {
  private String email;
  private String password;
  private Long storeId;

  public Long getStoreId() {
    return storeId;
  }

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }

  public String getEmail() {

    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
