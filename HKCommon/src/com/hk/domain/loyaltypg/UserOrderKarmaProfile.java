package com.hk.domain.loyaltypg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "user_order_karma_profile")
public class UserOrderKarmaProfile {

	@EmbeddedId
	private UserOrderKey userOrderKey;

	@Column(name = "creation_time", updatable = false)
	private Date creationTime = new Date();

	@Version
	@Column(name = "update_time")
	private Date updateTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type")
	private TransactionType transactionType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private karmaPointStatus status;

	@Column(name = "points")
	private Double karmaPints;
	
	public UserOrderKey getUserOrderKey() {
		return userOrderKey;
	}
	
	public void setUserOrderKey(UserOrderKey userOrderKey) {
		this.userOrderKey = userOrderKey;
	}
	
	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public karmaPointStatus getStatus() {
		return status;
	}

	public void setStatus(karmaPointStatus status) {
		this.status = status;
	}

	public Double getKarmaPints() {
		return karmaPints;
	}

	public void setKarmaPints(Double karmaPints) {
		this.karmaPints = karmaPints;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public static enum karmaPointStatus {
		APPROVED, PENDING, EXPIRED;
	}
	
	public static enum TransactionType {
		DEBIT, CREDIT;
	}
}
