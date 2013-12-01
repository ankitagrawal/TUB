package com.hk.impl.dto.mail;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: May 30, 2012
 * Time: 4:20:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserEmailDto {
  private String login;
  private String email;
  private String name;
  private String gender;
  private Date lastLoginDate;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getLastLoginDate() {
    return lastLoginDate;
  }

  public void setLastLoginDate(Date lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }
}
