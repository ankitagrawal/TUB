package com.hk.domain.subscription;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SubscriptionOrder generated by hbm2java
 */
@Entity
@Table(name="subscription_order")
public class SubscriptionOrder  implements java.io.Serializable {



    @Id @GeneratedValue(strategy=IDENTITY)

    @Column(name="id", unique=true, nullable=false)
    private Long id;


    @Column(name="subscription_id", nullable=false)
    private Long subscriptionId;


    @Column(name="base_order_id", nullable=false)
    private Long baseOrderId;


    @Column(name="subscription_order_status_id", nullable=false)
    private Long subscriptionOrderStatusId;


    @Column(name="hk_price_now", nullable=false, precision=10)
    private Double hkPriceNow;


    @Column(name="hk_discount_now", nullable=false, precision=7, scale=4)
    private Double hkDiscountNow;


    @Column(name="marked_price_now", nullable=false, precision=10)
    private Double markedPriceNow;


    @Column(name="cost_price_now", nullable=false, precision=10)
    private Double costPriceNow;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date", length=19)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_date", length=19)
    private Date updateDate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
    public Long getBaseOrderId() {
        return this.baseOrderId;
    }

    public void setBaseOrderId(Long baseOrderId) {
        this.baseOrderId = baseOrderId;
    }
    public Long getSubscriptionOrderStatusId() {
        return this.subscriptionOrderStatusId;
    }

    public void setSubscriptionOrderStatusId(Long subscriptionOrderStatusId) {
        this.subscriptionOrderStatusId = subscriptionOrderStatusId;
    }
    public Double getHkPriceNow() {
        return this.hkPriceNow;
    }

    public void setHkPriceNow(Double hkPriceNow) {
        this.hkPriceNow = hkPriceNow;
    }
    public Double getHkDiscountNow() {
        return this.hkDiscountNow;
    }

    public void setHkDiscountNow(Double hkDiscountNow) {
        this.hkDiscountNow = hkDiscountNow;
    }
    public Double getMarkedPriceNow() {
        return this.markedPriceNow;
    }

    public void setMarkedPriceNow(Double markedPriceNow) {
        this.markedPriceNow = markedPriceNow;
    }
    public Double getCostPriceNow() {
        return this.costPriceNow;
    }

    public void setCostPriceNow(Double costPriceNow) {
        this.costPriceNow = costPriceNow;
    }
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }




}


