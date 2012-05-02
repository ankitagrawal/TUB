package com.hk.domain;
// Generated Jan 28, 2012 3:13:58 PM by Hibernate Tools 3.2.4.CR1


import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.hk.domain.pk.TransactionFooterPk;

/**
 * TransactionFooter generated by hbm2java
 */
@Entity
@Table(name = "transaction_footer", uniqueConstraints = @UniqueConstraint(columnNames = {"vch_code", "s_no"}))
public class TransactionFooter implements java.io.Serializable {


	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "vchCode", column = @Column(name = "vch_code", nullable = false)),
		@AttributeOverride(name = "sNo", column = @Column(name = "s_no", nullable = false))})
	private TransactionFooterPk pk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vch_code", nullable = false, insertable = false, updatable = false)
	private TransactionHeader transactionHeader;

	@Column(name = "s_no", nullable = false, insertable = false, updatable = false)
	private Long sNo;

	@Column(name = "type")
	private Byte type;

	@Column(name = "bill_sundry_name", length = 40)
	private String billSundryName;

	@Column(name = "percent")
	private Long percent;

	@Column(name = "amount")
	private Double amount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, length = 19)
	private Date createDate;

	public TransactionFooterPk getId() {
		return this.pk;
	}

	public void setId(TransactionFooterPk pk) {
		this.pk = pk;
	}

	public void setId(Long vchCode, Long sNo) {
		this.pk = new TransactionFooterPk(vchCode, sNo);
	}

	public TransactionHeader getTransactionHeader() {
		return this.transactionHeader;
	}

	public void setTransactionHeader(TransactionHeader transactionHeader) {
		this.transactionHeader = transactionHeader;
	}

	public Long getSNo() {
		return this.sNo;
	}

	public void setSNo(Long sNo) {
		this.sNo = sNo;
	}

	public Byte getType() {
		return this.type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getBillSundryName() {
		return this.billSundryName;
	}

	public void setBillSundryName(String billSundryName) {
		this.billSundryName = billSundryName;
	}

	public Long getPercent() {
		return this.percent;
	}

	public void setPercent(Long percent) {
		this.percent = percent;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
