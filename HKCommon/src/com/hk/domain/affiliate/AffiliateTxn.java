package com.hk.domain.affiliate;

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

import com.hk.domain.order.Order;

@Entity
@Table(name="affiliate_txn")
public class AffiliateTxn {

  @Id
  @Column(name="id",nullable=false)
  @GeneratedValue(strategy=GenerationType.AUTO)
  Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date", length = 19)
  private Date date;

  @Column(name="amount",nullable=false)
  Double amount;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="affiliate_id",nullable=false)
  Affiliate affiliate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="base_order_id")
  Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "affiliate_txn_type_id",nullable = false)
  AffiliateTxnType affiliateTxnType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }


  public Affiliate getAffiliate() {
    return affiliate;
  }

  public void setAffiliate(Affiliate affiliate) {
    this.affiliate = affiliate;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public AffiliateTxnType getAffiliateTxnType() {
    return affiliateTxnType;
  }

  public void setAffiliateTxnType(AffiliateTxnType affiliateTxnType) {
    this.affiliateTxnType = affiliateTxnType;
  }
}
