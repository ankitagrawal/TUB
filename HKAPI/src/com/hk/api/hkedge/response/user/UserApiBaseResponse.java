package com.hk.api.hkedge.response.user;

import com.hk.api.hkedge.response.AbstractApiBaseResponse;

import java.util.HashSet;
import java.util.Set;


public class UserApiBaseResponse extends AbstractApiBaseResponse {

  private Long id;
  private String name;

  private Set<String> roles = new HashSet<String>();


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }
}
