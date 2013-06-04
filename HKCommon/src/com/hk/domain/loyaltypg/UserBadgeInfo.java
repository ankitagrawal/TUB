package com.hk.domain.loyaltypg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.hk.domain.user.User;

/**
 * @author Ankit Chhabra
 *
 */
@Entity
@Table(name = "user_badge_info")
public class UserBadgeInfo {
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", unique=true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "badge_id")
	private Badge badge;
	
	@Column(name = "creation_time", updatable = false)
	private Date creationTime = new Date();

	@Version
	@Column(name = "updation_time")
	private Date updationTime;

	@Column(name = "card_number")
	private String cardNumber;
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Badge getBadge() {
		return this.badge;
	}

	public void setBadge(Badge badge) {
		this.badge = badge;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdationTime() {
		return this.updationTime;
	}

	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return this.cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
}
