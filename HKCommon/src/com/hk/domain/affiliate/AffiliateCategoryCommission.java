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

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Jul 27, 2011
 * Time: 11:39:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "affiliate_category_commission")
public class AffiliateCategoryCommission {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "commission_first", length = 100, scale=0)
  private Double commissionFirstTime;

  @Column(name = "commission_second", length = 100, scale=0)
  private Double commissionLatterTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "affiliate_category_name", nullable = false)
  private AffiliateCategory affiliateCategory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "affiliate_id", nullable = false)
  private Affiliate affiliate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AffiliateCategory getAffiliateCategory() {
    return affiliateCategory;
  }

  public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
    this.affiliateCategory = affiliateCategory;
  }

  public Double getCommissionFirstTime() {
    return commissionFirstTime;
  }

  public void setCommissionFirstTime(Double commissionFirstTime) {
    this.commissionFirstTime = commissionFirstTime;
  }

  public Double getCommissionLatterTime() {
    return commissionLatterTime;
  }

  public void setCommissionLatterTime(Double commissionLatterTime) {
    this.commissionLatterTime = commissionLatterTime;
  }

  public Affiliate getAffiliate() {
    return affiliate;
  }

  public void setAffiliate(Affiliate affiliate) {
    this.affiliate = affiliate;
  }
}