package com.hk.domain.shippingOrder;
// Generated 27 Jun, 2013 1:39:13 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * FixedShippingOrder generated by hbm2java
 */
@Entity
@Table(name = "fixed_shipping_order")
public class FixedShippingOrder implements java.io.Serializable {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "closed_by", nullable = true)
  private User closedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_order_id", nullable = false)
  private ShippingOrder shippingOrder;


  @Column(name = "remarks", nullable = false, length = 65535)
  private String remarks;


  @Column(name = "status", nullable = false, length = 45)
  private String status;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date", nullable = false, length = 19)
  private Date updateDate;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public User getClosedBy() {
    return closedBy;
  }

  public void setClosedBy(User closedBy) {
    this.closedBy = closedBy;
  }

  public ShippingOrder getShippingOrder() {
    return this.shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public String getRemarks() {
    return this.remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return this.updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }


}


