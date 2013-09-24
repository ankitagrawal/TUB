package com.hk.domain.reversePickupOrder;


import com.akube.framework.gson.JsonSkip;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/18/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "reverse_pickup_order")
public class ReversePickupOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "reverse_pickup_order_id" ,unique = true ,nullable = false)
    private  String reversePickupId;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id", nullable = false)
    private ShippingOrder shippingOrder;

    @Column(name = "amount")
    private Double amount;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false)
    private Date createDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "close_dt")
    private Date closedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_pickup_status_id", nullable = false)
    private ReversePickupStatus reversePickupStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_pickup_type_id", nullable = false)
    private ReversePickupType reversePickupType;

    @Column(name = "courier_managed_by")
    private Long courierManagedBy;


    @Column(name = "courier_name")
    private String courierName;

    @Column(name = "tracking_number", length = 70)
    private String trackingNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pickup_time")
    private Date pickupTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reversePickupOrder")
    private List<RpLineItem> rpLineItems = new ArrayList<RpLineItem>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ReversePickupStatus getReversePickupStatus() {
        return reversePickupStatus;
    }

    public void setReversePickupStatus(ReversePickupStatus reversePickupStatus) {
        this.reversePickupStatus = reversePickupStatus;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public List<RpLineItem> getRpLineItems() {
        return rpLineItems;
    }

    public void setRpLineItems(List<RpLineItem> rpLineItems) {
        this.rpLineItems = rpLineItems;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public Long getCourierManagedBy() {
        return courierManagedBy;
    }

    public void setCourierManagedBy(Long courierManagedBy) {
        this.courierManagedBy = courierManagedBy;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    @Override
    public String toString() {
        return this.id != null ? this.id.toString() : "";
    }

    public String getReversePickupId() {
        return reversePickupId;
    }

    public void setReversePickupId(String reversePickupId) {
        this.reversePickupId = reversePickupId;
    }

    public ReversePickupType getReversePickupType() {
        return reversePickupType;
    }

    public void setReversePickupType(ReversePickupType reversePickupType) {
        this.reversePickupType = reversePickupType;
    }
}
