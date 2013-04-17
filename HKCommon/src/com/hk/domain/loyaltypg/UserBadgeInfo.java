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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.akube.framework.gson.JsonSkip;
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
	
	@Column(name = "credited_points")
	private Double creditedPoints;
	
	@Column(name = "debited_points")
	private Double debitedPoints;
	
    @JsonSkip
    @Temporal(TemporalType.DATE)
    @Column(name = "creation_time", nullable = true, length = 19)
	private Date creationTime;
	
    @JsonSkip
    @Temporal(TemporalType.DATE)
    @Column(name = "updation_time", nullable = true, length = 19)
    private Date updationTime;
	
    @JsonSkip
    @Temporal(TemporalType.DATE)
    @Column(name = "points_revision_date", nullable = true, length = 19)
    private Date pointsRevisionDate;

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
	 * @return the creditedPoints
	 */
	public Double getCreditedPoints() {
		return this.creditedPoints;
	}

	/**
	 * @param creditedPoints the creditedPoints to set
	 */
	public void setCreditedPoints(Double creditedPoints) {
		this.creditedPoints = creditedPoints;
	}

	/**
	 * @return the debitedPoints
	 */
	public Double getDebitedPoints() {
		return this.debitedPoints;
	}

	/**
	 * @param debitedPoints the debitedPoints to set
	 */
	public void setDebitedPoints(Double debitedPoints) {
		this.debitedPoints = debitedPoints;
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

	/**
	 * @return the pointsRevisionDate
	 */
	public Date getPointsRevisionDate() {
		return this.pointsRevisionDate;
	}

	/**
	 * @param pointsRevisionDate the pointsRevisionDate to set
	 */
	public void setPointsRevisionDate(Date pointsRevisionDate) {
		this.pointsRevisionDate = pointsRevisionDate;
	}
	
	
	
}
