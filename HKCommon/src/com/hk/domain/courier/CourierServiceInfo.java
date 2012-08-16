package com.hk.domain.courier;
// Generated Dec 26, 2011 1:31:41 PM by Hibernate Tools 3.2.4.CR1


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.core.Pincode;

/**
 * CourierServiceInfo generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "courier_service_info")
public class CourierServiceInfo implements java.io.Serializable {


  @Id
  @GeneratedValue(strategy = IDENTITY)

  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pincode_id", nullable = false)
  private Pincode pincode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "courier_id", nullable = false)
  private Courier courier;


  @Column(name = "cod_available", nullable = false)
  private boolean codAvailable;


  @Column(name = "routing_code", length = 45)
  private String routingCode;


  @Column(name = "is_preferred", nullable = true)
  private Boolean preferred;


  @Column(name = "is_preferred_cod", nullable = true)
  private Boolean preferredCod;

  @Column(name = "is_deleted", nullable = true)
  private Boolean deleted;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Pincode getPincode() {
    return this.pincode;
  }

  public void setPincode(Pincode pincode) {
    this.pincode = pincode;
  }

  public Courier getCourier() {
    return this.courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }

  public boolean isCodAvailable() {
    return this.codAvailable;
  }

  public void setCodAvailable(boolean codAvailable) {
    this.codAvailable = codAvailable;
  }

  public String getRoutingCode() {
    return this.routingCode;
  }

  public void setRoutingCode(String routingCode) {
    this.routingCode = routingCode;
  }

  public boolean isPreferred() {
    return this.preferred;
  }

  public void setPreferred(boolean preferred) {
    this.preferred = preferred;
  }

  public Boolean isPreferredCod() {
    return this.preferredCod;
  }

  public void setPreferredCod(Boolean preferredCod) {
    this.preferredCod = preferredCod;
  }

  public Boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }
}


