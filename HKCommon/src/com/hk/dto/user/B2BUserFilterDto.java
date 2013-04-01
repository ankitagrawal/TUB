package com.hk.dto.user;

import java.util.Date;

public class B2BUserFilterDto {

  private Long id;
  private String login;
  private String email;
  private Date createDateFrom;
  private Date createDateTo;
  private Date lastLoginDateFrom;
  private Date lastLoginDateTo;
  private String name;
  private String userHash;
  private String tin;
  private String dlNumber;
  private String phone;

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

  public Date getCreateDateFrom() {
    return createDateFrom;
  }

  public void setCreateDateFrom(Date createDateFrom) {
    this.createDateFrom = createDateFrom;
  }

  public Date getCreateDateTo() {
    return createDateTo;
  }

  public void setCreateDateTo(Date createDateTo) {
    this.createDateTo = createDateTo;
  }

  public Date getLastLoginDateFrom() {
    return lastLoginDateFrom;
  }

  public void setLastLoginDateFrom(Date lastLoginDateFrom) {
    this.lastLoginDateFrom = lastLoginDateFrom;
  }

  public Date getLastLoginDateTo() {
    return lastLoginDateTo;
  }

  public void setLastLoginDateTo(Date lastLoginDateTo) {
    this.lastLoginDateTo = lastLoginDateTo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUserHash() {
    return userHash;
  }

  public void setUserHash(String userHash) {
    this.userHash = userHash;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTin() {
	return tin;
}

public void setTin(String tin) {
	this.tin = tin;
}

public String getDlNumber() {
	return dlNumber;
}

public void setDlNumber(String dlNumber) {
	this.dlNumber = dlNumber;
}
  
public String getPhone() {
	return phone;
}

public void setPhone(String phone) {
	this.phone = phone;
}

@Override public String toString() {
    return "";
  }

}
