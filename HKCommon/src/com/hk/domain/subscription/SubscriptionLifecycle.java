package com.hk.domain.subscription;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.user.User;

/**
 * SubscriptionLifecycle generated by hbm2java
 */
@Entity
@Table(name="subscription_lifecycle")
public class SubscriptionLifecycle  implements java.io.Serializable {



    @Id @GeneratedValue(strategy=IDENTITY)

    @Column(name="id", unique=true, nullable=false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_lifecycle_activity_id", nullable = false)
    private SubscriptionLifecycleActivity subscriptionLifecycleActivity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date", nullable=false, length=19)
    private Date date;


    @Column(name="comments", length=65535)
    private String comments;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Subscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
    public SubscriptionLifecycleActivity getSubscriptionLifecycleActivity() {
        return this.subscriptionLifecycleActivity;
    }

    public void setSubscriptionLifecycleActivity(SubscriptionLifecycleActivity subscriptionLifecycleActivity) {
        this.subscriptionLifecycleActivity = subscriptionLifecycleActivity;
    }
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }




}