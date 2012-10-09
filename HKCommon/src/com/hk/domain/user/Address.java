package com.hk.domain.user;
// Generated 25 Mar, 2011 11:57:39 AM by Hibernate Tools 3.2.4.CR1


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.akube.framework.gson.JsonSkip;
import org.hibernate.annotations.SQLDelete;

import com.hk.domain.courier.Courier;

/**
 * Address generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "address")
@SQLDelete(sql = "UPDATE line_item SET deleted = 1  WHERE id = ? ")
public class Address implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "default_courier_id")
  private Courier courier;

  @Column(name = "name", nullable = false, length = 150)
  private String name;

  @Column(name = "line1", nullable = false, length = 200)
  private String line1;

  @Column(name = "line2", length = 200)
  private String line2;

  @Column(name = "city", nullable = false, length = 80)
  private String city;

  @Column(name = "state", nullable = false, length = 60)
  private String state;

  @Column(name = "pin", nullable = false, length = 6)
  private String pin;

  @Column(name = "phone", nullable = false, length = 30)
  private String phone;

  @Column(name = "deleted", nullable = false, length = 30)
  private Boolean deleted;

  @JsonSkip
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Transient
  private boolean selected;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Courier getCourier() {
    return this.courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLine1() {
    return this.line1;
  }

  public void setLine1(String line1) {
    this.line1 = line1;
  }

  public String getLine2() {
    return this.line2;
  }

  public void setLine2(String line2) {
    this.line2 = line2;
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

  public String getPin() {
    return this.pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public Boolean isDeleted() {
    return deleted;
  }
  
  public Boolean getDeleted() {
      return deleted;
    }


  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Address)) return false;

    Address address = (Address) o;

    if (id != null ? !id.equals(address.id) : address.id != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
}


