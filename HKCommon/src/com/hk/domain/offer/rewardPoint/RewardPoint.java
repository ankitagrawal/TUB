package com.hk.domain.offer.rewardPoint;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Comparator;
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

import com.hk.domain.order.Order;
import com.hk.domain.user.User;

/**
 * RewardPoint generated by hbm2java
 */
@Entity
@Table(name = "reward_point")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class RewardPoint implements java.io.Serializable, Comparator<RewardPoint>, Comparable<RewardPoint> {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "referred_user_id", nullable = true)
  private User referredUser;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reward_point_status_id", nullable = false)
  private RewardPointStatus rewardPointStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reward_point_mode_id")
  private RewardPointMode rewardPointMode;

  @Column(name = "value", nullable = false)
  private Double value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "referred_order_id", nullable = true)
  private Order referredOrder;

  @Column(name = "comment", nullable = true)
  private String comment;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getReferredUser() {
    return this.referredUser;
  }

  public void setReferredUser(User referredUser) {
    this.referredUser = referredUser;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public RewardPointStatus getRewardPointStatus() {
    return rewardPointStatus;
  }

  public void setRewardPointStatus(RewardPointStatus rewardPointStatus) {
    this.rewardPointStatus = rewardPointStatus;
  }

  public RewardPointMode getRewardPointMode() {
    return rewardPointMode;
  }

  public void setRewardPointMode(RewardPointMode rewardPointMode) {
    this.rewardPointMode = rewardPointMode;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Order getReferredOrder() {
    return referredOrder;
  }

  public void setReferredOrder(Order referredOrder) {
    this.referredOrder = referredOrder;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return id != null ? id.toString() : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RewardPoint)) return false;

    RewardPoint that = (RewardPoint) o;

    if (!id.equals(that.id)) return false;
    return user.equals(that.user);

  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + user.hashCode();
    return result;
  }

  public int compare(RewardPoint r1, RewardPoint r2) {
    if (r1.getCreateDate().before(r2.getCreateDate())) return -1;
    if (r1.getCreateDate().after(r2.getCreateDate())) return 1;
    return 0;
  }

  public int compareTo(RewardPoint r) {
    if (this.createDate.before(r.getCreateDate())) return -1;
    if (this.createDate.after(r.getCreateDate())) return 1;
    return 0;
  }
}


