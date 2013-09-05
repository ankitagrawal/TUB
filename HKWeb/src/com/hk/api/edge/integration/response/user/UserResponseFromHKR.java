package com.hk.api.edge.integration.response.user;


import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class UserResponseFromHKR extends BaseUserResponseFromHKR {


  private String login;
  private String email;
  private String pwd;

  private Set<String> permissions = new HashSet<String>();
  private String birthDt;
  private String gender;

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

  public String getBirthDt() {
    return birthDt;
  }

  public void setBirthDt(String birthDt) {
    this.birthDt = birthDt;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }


  @Override
  protected String[] getKeys() {
    return new String[]{
        "login",
        "email",
        "pwd",
        "permissions",
        "birthDt",
        "gender",
        "id",
        "nm",
        "roles",
        "exception",
        "msgs"
    };
  }

  @Override
  protected Object[] getValues() {
    return new Object[]{
        this.login,
        this.email,
        this.pwd,
        this.permissions,
        this.birthDt,
        this.gender,
        this.getId(),
        this.getNm(),
        this.getRoles(),
        this.exception,
        this.msgs
    };
  }
}
