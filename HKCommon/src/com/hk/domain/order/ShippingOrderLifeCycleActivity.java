package com.hk.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shipping_order_lifecycle_activity")
public class ShippingOrderLifeCycleActivity {
  
  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Long id;


  @Column(name = "name", nullable = false, length = 100)
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
