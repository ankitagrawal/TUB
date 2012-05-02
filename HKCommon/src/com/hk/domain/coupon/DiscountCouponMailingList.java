package com.hk.domain.coupon;
// Generated Aug 23, 2011 9:24:25 AM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DiscountCouponMailingList generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "discount_coupon_mailing_list")
public class DiscountCouponMailingList implements java.io.Serializable {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", length = 65)
  private String name;

  @Column(name = "mobile", length = 25)
  private String mobile;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "src_url")
  private String srcUrl;

  @Column(name = "category", length = 65)
  private String category;

  @Column(name = "coupon_code", nullable = false, length = 45)
  private String couponCode;

  @Column(name = "subscribe_mobile")
  private boolean subscribeMobile;

  @Column(name = "subscribe_email")
  private boolean subscribeEmail;

  @Column(name = "comments")
  private String comments;

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

  public String getMobile() {
    return this.mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSrcUrl() {
    return this.srcUrl;
  }

  public void setSrcUrl(String srcUrl) {
    this.srcUrl = srcUrl;
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
  }

  public boolean isSubscribeMobile() {
    return subscribeMobile;
  }

  public void setSubscribeMobile(boolean subscribeMobile) {
    this.subscribeMobile = subscribeMobile;
  }

  public boolean isSubscribeEmail() {
    return subscribeEmail;
  }

  public void setSubscribeEmail(boolean subscribeEmail) {
    this.subscribeEmail = subscribeEmail;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}


