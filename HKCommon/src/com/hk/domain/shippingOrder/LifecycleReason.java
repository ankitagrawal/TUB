package com.hk.domain.shippingOrder;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.OrderLifecycle;
import com.hk.domain.order.ShippingOrderLifecycle;

import javax.persistence.*;
import java.util.Date;

/*
 * User: Pratham
 * Date: 25/03/13  Time: 23:00
*/
@Entity
@Table(name = "lifecycle_reason")
public class LifecycleReason implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_lifecycle_id")
    public ShippingOrderLifecycle shippingOrderLifecycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_lifecycle_id")
    public OrderLifecycle orderLifecycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_id", nullable = false)
    private Reason reason;

    @Column(name = "primary_attribution")
    private String primaryAttribution;

    @Column(name = "secondary_attribution")
    private String secondaryAttribution;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingOrderLifecycle getShippingOrderLifecycle() {
        return shippingOrderLifecycle;
    }

    public void setShippingOrderLifecycle(ShippingOrderLifecycle shippingOrderLifecycle) {
        this.shippingOrderLifecycle = shippingOrderLifecycle;
    }

    public OrderLifecycle getOrderLifecycle() {
        return orderLifecycle;
    }

    public void setOrderLifecycle(OrderLifecycle orderLifecycle) {
        this.orderLifecycle = orderLifecycle;
    }

    public String getPrimaryAttribution() {
        return primaryAttribution;
    }

    public void setPrimaryAttribution(String primaryAttribution) {
        this.primaryAttribution = primaryAttribution;
    }

    public String getSecondaryAttribution() {
        return secondaryAttribution;
    }

    public void setSecondaryAttribution(String secondaryAttribution) {
        this.secondaryAttribution = secondaryAttribution;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
