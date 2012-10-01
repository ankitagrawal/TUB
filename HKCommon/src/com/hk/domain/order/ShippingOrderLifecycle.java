package com.hk.domain.order;

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

import com.hk.domain.user.User;

@Entity
@Table(name = "shipping_order_lifecycle")

public class ShippingOrderLifecycle implements java.io.Serializable, Comparable<ShippingOrderLifecycle> {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_order_lifecycle_activity_id", nullable = false)
  private ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_order_id")
  private ShippingOrder shippingOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_by_user_id", nullable = false)
  private User user;

  @Column(name = "comments")
  private String comments;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "activity_date", nullable = false, length = 19)
  private Date activityDate;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity() {
    return shippingOrderLifeCycleActivity;
  }

  public void setShippingOrderLifeCycleActivity(ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity) {
    this.shippingOrderLifeCycleActivity = shippingOrderLifeCycleActivity;
  }

  public ShippingOrder getOrder() {
    return shippingOrder;
  }

  public void setOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Date getActivityDate() {
    return this.activityDate;
  }

  public void setActivityDate(Date activityDate) {
    this.activityDate = activityDate;
  }

	public int compareTo(ShippingOrderLifecycle shippingOrderLifecycle) {
		if (this.getActivityDate() != null && shippingOrderLifecycle.getActivityDate() != null) {
			int val = this.getActivityDate().compareTo(shippingOrderLifecycle.getActivityDate());
			if (val == 0) {
				if (this.getId() != null && shippingOrderLifecycle.getId() != null) {
					return this.getId().compareTo(shippingOrderLifecycle.getId());
				}
			}
			return val;
		}
		return 0;
	}
}
