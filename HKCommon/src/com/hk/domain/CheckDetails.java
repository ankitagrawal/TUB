package com.hk.domain;

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

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxn;

@Entity
@Table(name = "check_details")
public class CheckDetails {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "check_no", nullable = false, length = 45)
  private String checkNo;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "issue_date", length = 19)
  private Date issueDate;

	@Column(name = "tds", nullable = false, precision = 6)
	private Double tds;

	@Column(name = "bank_name", nullable = false, length = 20)
  private String bankName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "affiliate_id")
  private Affiliate affiliate;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "affiliate_txn_id")
  private AffiliateTxn affiliateTxn;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCheckNo() {
    return checkNo;
  }

  public void setCheckNo(String checkNo) {
    this.checkNo = checkNo;
  }

  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public Affiliate getAffiliate() {
    return affiliate;
  }

  public void setAffiliate(Affiliate affiliate) {
    this.affiliate = affiliate;
  }

  public AffiliateTxn getAffiliateTxn() {
    return affiliateTxn;
  }

  public void setAffiliateTxn(AffiliateTxn affiliateTxn) {
    this.affiliateTxn = affiliateTxn;
  }

	public Double getTds() {
		return tds;
	}

	public void setTds(Double tds) {
		this.tds = tds;
	}
}
