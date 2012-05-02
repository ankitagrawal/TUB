package com.hk.domain.inventory;
// Generated Dec 15, 2011 3:32:41 PM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * GrnStatus generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "grn_status")
public class GrnStatus implements java.io.Serializable {


  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }

}


