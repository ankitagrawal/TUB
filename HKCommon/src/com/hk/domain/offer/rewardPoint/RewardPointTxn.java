package com.hk.domain.offer.rewardPoint;

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
import javax.persistence.Transient;

import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "reward_point_txn")
public class RewardPointTxn implements java.io.Serializable, Comparable<RewardPointTxn> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long               id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_point_txn_type_id", nullable = false)
    private RewardPointTxnType rewardPointTxnType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_point_id", nullable = false)
    private RewardPoint        rewardPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "redeemed_order_id", nullable = true)
    private Order              redeemedOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User               user;

    @Column(name = "value", nullable = false)
    private Double             value;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "txn_date", nullable = false, length = 19)
    private Date               txnDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false, length = 19)
    private Date               expiryDate;

    @SuppressWarnings("unused")
    @Transient
    private boolean            expired;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RewardPointTxnType getRewardPointTxnType() {
        return this.rewardPointTxnType;
    }

    public void setRewardPointTxnType(RewardPointTxnType rewardPointTxnType) {
        this.rewardPointTxnType = rewardPointTxnType;
    }

    public RewardPoint getRewardPoint() {
        return this.rewardPoint;
    }

    public void setRewardPoint(RewardPoint rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public Order getRedeemedOrder() {
        return this.redeemedOrder;
    }

    public void setRedeemedOrder(Order redeemedOrder) {
        this.redeemedOrder = redeemedOrder;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getTxnDate() {
        return this.txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return expiryDate.before(new Date());
    }

    public int compareTo(RewardPointTxn rewardPointTxn) {
        if (this.id < rewardPointTxn.getId())
            return -1;
        if (this.id > rewardPointTxn.getId())
            return 1;
        return 0;
    }

    public boolean isType(EnumRewardPointTxnType txnType) {
        return this.rewardPointTxnType.getId().equals(txnType.getId());
    }
}
