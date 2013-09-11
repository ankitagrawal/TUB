package com.hk.domain.core;


import com.hk.domain.courier.Zone;
import com.hk.domain.hkDelivery.Hub;

import javax.persistence.*;

/**
 * Pincode generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pincode", uniqueConstraints = @UniqueConstraint(columnNames = "pincode"))
public class Pincode implements java.io.Serializable {


  @Id
  @Column(name = "pincode", unique = true, nullable = false)
  private String pincode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id")
  private City city;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "state_id")
  private State state;

  @Column(name = "region", nullable = true, length = 25)
  private String region;

  @Column(name = "locality", length = 25)
  private String locality;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "zone_id", nullable = false)
  private Zone zone;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="nearest_hub_id", nullable = true)
  private Hub nearestHub;

  @Column(name = "last_mile_cost")
  private Double lastMileCost;

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public String getLocality() {
    return this.locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Zone getZone() {
    return zone;
  }

  public void setZone(Zone zone) {
    this.zone = zone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Pincode pincode1 = (Pincode) o;

    return pincode.equals(pincode1.pincode);
  }

  @Override
  public int hashCode() {
    return pincode.hashCode();
  }

  @Override
  public String toString() {
    return pincode == null ? "" : pincode;
  }

  public Hub getNearestHub() {
    return nearestHub;
  }

  public void setNearestHub(Hub nearestHub) {
    this.nearestHub = nearestHub;
  }

  public Double getLastMileCost() {
    return lastMileCost;
  }

  public void setLastMileCost(Double lastMileCost) {
    this.lastMileCost = lastMileCost;
  }
}


