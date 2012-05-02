package com.hk.domain.offer;
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

import org.joda.time.DateTime;

import com.hk.domain.coupon.Coupon;
import com.hk.domain.user.User;


@SuppressWarnings("serial")
@Entity
@Table(name = "offer_instance")
public class OfferInstance implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offer_id", nullable = false)
  private Offer offer;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "end_date", length = 19)
  private Date endDate;

  @Column(name = "active", nullable = false)
  private boolean active;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Transient
  private Double discountAmountUsed;
  @Transient
  private Double discountAmountUsedTotal;

  public OfferInstance() {
  }

  public OfferInstance(Offer offer, Coupon coupon, User user) {
    this.offer = offer;
    this.active = true;
    this.discountAmountUsed = 0D;
    this.discountAmountUsedTotal = 0D;
    this.user = user;
    this.coupon = coupon;
  }

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

  public Coupon getCoupon() {
    return this.coupon;
  }

  public void setCoupon(Coupon coupon) {
    this.coupon = coupon;
  }

  public Offer getOffer() {
    return this.offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }

  public Date getEndDate() {
    return this.endDate != null ? this.endDate : getOffer().getEndDate();
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }


  public boolean isValid() {
    if (!this.active) return false;  // if offer is not active return early

//    if (allowedOrders <= 0) { // used times is more than allowed times or offer got expired
//      this.setActive(false);
//      return false;
//    }
//

    if ((getEndDate() != null && new DateTime(getEndDate()).isBeforeNow())) {
      this.setActive(false);
      return false;
    }

    return !new DateTime(createDate).isAfterNow();
  }

  public Double getDiscountAmountUsed() {
    return discountAmountUsed;
  }

  public Double getDiscountAmountUsedTotal() {
    return discountAmountUsedTotal;
  }

  public void setDiscountAmountUsed(double discountAmountUsed) {
    this.discountAmountUsed = discountAmountUsed;
  }

  public void setDiscountAmountUsedTotal(double discountAmountUsedTotal) {
    this.discountAmountUsedTotal = discountAmountUsedTotal;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
}


