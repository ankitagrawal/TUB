package com.hk.api.edge.ext.response.user;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserApiResponse extends UserApiBaseResponse{


  private String login;
  private String email;
  private String pwd;

  private Set<String> permissions = new HashSet<String>();
  private Date birthDt;
  private String gender ;

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

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public Set<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<String> permissions) {
    this.permissions = permissions;
  }

  public Date getBirthDt() {
    return birthDt;
  }

  public void setBirthDt(Date birthDt) {
    this.birthDt = birthDt;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }
}
