package com.hk.domain.affiliate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="affiliate_account_details")
public class AffiliateAccountDetails {

  @Id
  @Column(name="id",nullable=false)
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;

  @Column(name="name",nullable=false,length=45)
  private String name;

  @Column(name="account_no",nullable=false,length=100)
  private String accountNo;


  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="affiliate_id",nullable=false)
  private Affiliate affiliate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }


  public Affiliate getAffiliate() {
    return affiliate;
  }

  public void setAffiliate(Affiliate affiliate) {
    this.affiliate = affiliate;
  }
}
