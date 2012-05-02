package com.hk.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.user.Address;


@SuppressWarnings("serial")
@Entity
@Table(name = "locality_map")
public class LocalityMap implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id", unique = true, nullable = false)
  private Address address;

  @Column(name = "lattitude", precision = 9, scale = 6)
  private Double lattitude;

  @Column(name = "longitude", precision = 9, scale = 6)
  private Double longitude;

  public Double getLattitude() {
    return this.lattitude;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public void setLattitude(Double lattitude) {
    this.lattitude = lattitude;
  }

  public Double getLongitude() {
    return this.longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
}


