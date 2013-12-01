package com.hk.domain.coupon;


import static javax.persistence.GenerationType.AUTO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.user.User;


@SuppressWarnings("serial")
@Entity
@Table(name = "fbcoupon_user")
public class FbcouponUser implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "fbuid", nullable = false)
  private String fbuid;

  @Column(name = "app_id", nullable = false)
  private String appId;

  @Column(name = "access_token")
  private String accessToken;

  @Column(name = "scope")
  private String scope;

  @Column(name = "email", length = 60)
  private String email;

  @Column(name = "name", length = 60)
  private String name;

  @Column(name = "gender", length = 10)
  private String gender;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "dob", nullable = true, length = 19)
  private Date dob;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fbcouponUser")
  private Set<FbcouponUserCampaign> fbcouponUserCampaigns = new HashSet<FbcouponUserCampaign>(0);

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date", nullable = false, length = 19)
  private Date updateDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "token_create_date", nullable = true, length = 19)
  private Date tokenCreateDate;

  @Column(name = "expires_seconds")
  private Long expiresSeconds;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "associated_user_id", nullable = true)
  private User associatedUser;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFbuid() {
    return this.fbuid;
  }

  public void setFbuid(String fbuid) {
    this.fbuid = fbuid;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<FbcouponUserCampaign> getFbcouponUserCampaigns() {
    return this.fbcouponUserCampaigns;
  }

  public void setFbcouponUserCampaigns(Set<FbcouponUserCampaign> fbcouponUserCampaigns) {
    this.fbcouponUserCampaigns = fbcouponUserCampaigns;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public Date getTokenCreateDate() {
    return tokenCreateDate;
  }

  public void setTokenCreateDate(Date tokenCreateDate) {
    this.tokenCreateDate = tokenCreateDate;
  }

  public Long getExpiresSeconds() {
    return expiresSeconds;
  }

  public void setExpiresSeconds(Long expiresSeconds) {
    this.expiresSeconds = expiresSeconds;
  }

  public User getAssociatedUser() {
    return associatedUser;
  }

  public void setAssociatedUser(User associatedUser) {
    this.associatedUser = associatedUser;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getDob() {
    return dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
}


