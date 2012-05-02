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

import com.hk.domain.user.User;

@Entity
@Table(name="affiliate")
public class Affiliate {

  @Id
  @Column(name = "id",nullable=false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name="code", nullable=false, length=150)
  private String code;

  @Column(name = "check_in_favour_of",length = 150)
  private String checkInFavourOf;

  @Column(name="affiliate_expiry_date")
  private Date expiryDate;

  @Column(name="affiliate_pan_no")
   private String panNo;

  @Column(name="affiliate_website")
   private String websiteName;

  @Column(name="main_address_id")
  Long mainAddressId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCheckInFavourOf() {
    return checkInFavourOf;
  }

  public void setCheckInFavourOf(String checkInFavourOf) {
    this.checkInFavourOf = checkInFavourOf;
  }

  public Long getMainAddressId() {
    return mainAddressId;
  }

  public void setMainAddressId(Long mainAddressId) {
    this.mainAddressId = mainAddressId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public String getPanNo() {
    return panNo;
  }

  public void setPanNo(String panNo) {
    this.panNo = panNo;
  }

  public String getWebsiteName() {
    return websiteName;
  }

  public void setWebsiteName(String websiteName) {
    this.websiteName = websiteName;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
}
