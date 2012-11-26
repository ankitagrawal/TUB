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

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.user.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "order_lifecycle")
public class OrderLifecycle implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long                   id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_lifecycle_activity_id", nullable = false)
    private OrderLifecycleActivity orderLifecycleActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order                  order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_by_user_id", nullable = false)
    private User                   user;

    @Column(name = "comments")
    private String                 comments;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_date", nullable = false, length = 19)
    private Date                   activityDate;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date                   createDate = new Date();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderLifecycleActivity getOrderLifecycleActivity() {
        return this.orderLifecycleActivity;
    }

    public void setOrderLifecycleActivity(OrderLifecycleActivity orderLifecycleActivity) {
        this.orderLifecycleActivity = orderLifecycleActivity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /*
     * public ShippingOrder getShippingOrder() { return shippingOrder; } public void setShippingOrder(ShippingOrder
     * shippingOrder) { this.shippingOrder = shippingOrder; }
     */

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
