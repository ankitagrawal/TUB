package com.hk.domain.loyaltypg;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	private KarmaPointStatus status;

	@Column(name = "points")
	private Double karmaPoints;
		
	public UserOrderKey getUserOrderKey() {
		return this.userOrderKey;
	}
	
	public void setUserOrderKey(UserOrderKey userOrderKey) {
		this.userOrderKey = userOrderKey;
	}
	
	public TransactionType getTransactionType() {
		return this.transactionType;
	}
	
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public KarmaPointStatus getStatus() {
		return this.status;
	}

	public void setStatus(KarmaPointStatus status) {
		this.status = status;
	}

	public Double getKarmaPoints() {
		return this.karmaPoints;
	}

	public void setKarmaPoints(Double karmaPoints) {
		this.karmaPoints = karmaPoints;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public static enum KarmaPointStatus {
		APPROVED, PENDING, EXPIRED, CANCELED;
	}
	
	public static enum TransactionType {
		DEBIT, CREDIT;
	}
	
	
	/**
	 * This method is used only for the displaying points expiry on history page.
	 * @return expiryDate
	 */
	public String getExpiryDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.creationTime);
		cal.add(Calendar.YEAR, 2);
		if (TransactionType.DEBIT.equals(this.transactionType)) {
			return " ";
		} 
		return "Expiry on: " + new SimpleDateFormat("MMM dd,yyyy").format(cal.getTime());
	}

	/**
	 * This method is used only for the displaying points status on history page.
	 * @return 
	 */
	public String getStatusForHistory() {
		
		String statusForHistory = null;
		if (TransactionType.DEBIT.equals(this.transactionType)) {
			statusForHistory = "Redeemed";
		} else if (TransactionType.CREDIT.equals(this.transactionType)) {
			switch (this.status) {
			case PENDING:
				statusForHistory = "Awaited";
				break;
			case CANCELED:
				statusForHistory = "Cancelled";
				break;
			case APPROVED:
				statusForHistory = "Valid";
				break;
			default: 
				statusForHistory = "Expired";
			}
		}
		return statusForHistory;
	}
}
