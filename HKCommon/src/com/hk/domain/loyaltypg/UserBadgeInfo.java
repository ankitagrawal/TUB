/**
 * 
 */
package com.hk.domain.loyaltypg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "points_revision_time", nullable = true, length = 19)
    private Date pointsRevisionTime;
	
	
	
}
