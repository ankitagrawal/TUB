package com.hk.domain.feedback;

import com.hk.domain.order.Order;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/20/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "feedback")
public class Feedback implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "base_order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "recommend_to_friends")
	private Long recommendToFriends;

	@Column(name = "website_experience")
	private Long websiteExperience;

	@Column(name = "customer_care_experience")
	private Long customerCareExperience;

	@Column(name = "comments")
	private String comments;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "feedback_date", nullable = false)
    private Date feedbackDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getRecommendToFriends() {
		return recommendToFriends;
	}

	public void setRecommendToFriends(Long recommendToFriends) {
		this.recommendToFriends = recommendToFriends;
	}

	public Long getWebsiteExperience() {
		return websiteExperience;
	}

	public void setWebsiteExperience(Long websiteExperience) {
		this.websiteExperience = websiteExperience;
	}

	public Long getCustomerCareExperience() {
		return customerCareExperience;
	}

	public void setCustomerCareExperience(Long customerCareExperience) {
		this.customerCareExperience = customerCareExperience;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
}
