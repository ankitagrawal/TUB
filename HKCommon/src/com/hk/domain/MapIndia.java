package com.hk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "map_india")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class MapIndia implements java.io.Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "city", length = 90)
  private String city;

  @Column(name = "state", length = 90)
  private String state;

  @Column(name = "lattitude", precision = 9, scale = 6)
  private Double lattitude;

  @Column(name = "longitude", precision = 9, scale = 6)
  private Double longitude;

  @Column(name = "target_city")
  public Boolean targetCity;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return this.state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Double getLattitude() {
    return this.lattitude;
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

  public Boolean isTargetCity() {
    return targetCity;
  }

  public Boolean getTargetCity() {
    return true;
  }

  public void setTargetCity(Boolean targetCity) {
    this.targetCity = targetCity;
  }
}


