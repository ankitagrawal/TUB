/**
 * 
 */
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

	@Column(name = "valid_points")
	private Double validPoints;
	
    @Column(name = "creation_time", nullable = true, length = 19)
	private Date creationTime;
	
    @Column(name = "updation_time", nullable = true, length = 19)
    private Date updationTime;
	

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the badge
	 */
	public Badge getBadge() {
		return this.badge;
	}

	/**
	 * @param badge the badge to set
	 */
	public void setBadge(Badge badge) {
		this.badge = badge;
	}

	/**
	 * @return the validPoints
	 */
	public Double getValidPoints() {
		return this.validPoints;
	}

	/**
	 * @param validPoints the validPoints to set
	 */
	public void setValidPoints(Double validPoints) {
		this.validPoints = validPoints;
	}


	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return this.creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the updationTime
	 */
	public Date getUpdationTime() {
		return this.updationTime;
	}

	/**
	 * @param updationTime the updationTime to set
	 */
	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}

}
