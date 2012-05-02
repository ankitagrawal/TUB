package com.hk.domain.core;
// Generated Oct 18, 2011 12:19:36 AM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "product_variant_service_type")
public class ProductVariantServiceType implements java.io.Serializable {
  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", length = 45)
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
}


