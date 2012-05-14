package com.hk.domain.courier;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "courier_group")
public class CourierGroup implements java.io.Serializable {
  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", length = 45)
  private String name;

  @ManyToMany (fetch = FetchType.EAGER)
  @Fetch (value = FetchMode.SELECT)
  @JoinTable(name = "courier_group_has_courier",
      joinColumns = @JoinColumn(name = "courier_group_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "courier_id", referencedColumnName = "id")
  )
  private Set<Courier> couriers = new HashSet<Courier>();


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

  public Set<Courier> getCouriers() {
    return couriers;
  }

  public void setCouriers(Set<Courier> couriers) {
    this.couriers = couriers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CourierGroup)) return false;

    CourierGroup courierGroup = (CourierGroup) o;

    if (!name.equals(courierGroup.name)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}


