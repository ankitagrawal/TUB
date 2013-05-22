package com.hk.domain.loyaltypg;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.joda.time.DateTime;

import com.hk.domain.order.Order;
import com.hk.domain.user.User;

@Entity
@Table(name = "user_order_karma_profile")
public class UserOrderKarmaProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable=false)
	private Order order;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable=false)
	private User user;

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
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public static enum KarmaPointStatus {
		APPROVED, PENDING, EXPIRED, CANCELED, REWARDED, BONUS;
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
		if (TransactionType.DEBIT.equals(this.transactionType) ) {
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
	
			if (KarmaPointStatus.REWARDED.equals(this.status)) {
				statusForHistory = "Converted to Reward Points";
			} else if (KarmaPointStatus.CANCELED.equals(this.status)) {
				statusForHistory = "Order Cancelled (Points re-added)";
			} else {
				statusForHistory = "Redeemed";
			}
		} else if (TransactionType.CREDIT.equals(this.transactionType)) {
			switch (this.status) {
			case BONUS:
				statusForHistory = "Enrollment Bonus";
				break;
			case PENDING:
				statusForHistory = "Pending";
				break;
			case CANCELED:
				statusForHistory = "Cancelled";
				break;
			case APPROVED:
				if (this.creationTime.after(new DateTime().minusYears(2).toDate())) {
					statusForHistory = "Earned";
					break;
				}
			default: 
				statusForHistory = "Expired";
			}
		}
		return statusForHistory;
	}
}
