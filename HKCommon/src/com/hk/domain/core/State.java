package com.hk.domain.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 20, 2012
 * Time: 4:59:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "state")
public class State implements java.io.Serializable,Comparable<State> {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Long id;


  @Column(name = "name", nullable = false, length = 45)
  private String name;


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

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }

  public int compareTo(State state) {
    return this.getName().compareTo(state.getName());

  }
}
