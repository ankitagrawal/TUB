package com.hk.domain;
// Generated Jan 20, 2012 11:32:05 AM by Hibernate Tools 3.2.4.CR1


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@SuppressWarnings("serial")
@Entity
@Table(name = "doom_day")
public class DoomDay implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;


  @Column(name = "upc_variant_id", length = 45)
  private String upcVariantId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_stamp", length = 19)
  private Date timeStamp;

  @Column(name = "qty")
  private Long qty;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUpcVariantId() {
    return this.upcVariantId;
  }

  public void setUpcVariantId(String upcVariantId) {
    this.upcVariantId = upcVariantId;
  }

  public Date getTimeStamp() {
    return this.timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public Long getQty() {
    return this.qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }


}


