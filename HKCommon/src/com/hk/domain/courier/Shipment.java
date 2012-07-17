package com.hk.domain.courier;

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
import javax.persistence.Transient;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.order.ShippingOrder;

@SuppressWarnings("serial")
@Entity
@Table(name = "shipment")
public class Shipment implements java.io.Serializable, Comparable<Shipment> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_size_id")
    private BoxSize boxSize;

    @Column(name = "box_weight")
    private Double boxWeight;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awb_id")
    private Awb awb;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ship_date", length = 19)
    private Date shipDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivery_date", length = 19)
    private Date deliveryDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "return_date", length = 19)
    private Date returnDate;

    @Column(name = "email_sent")
    private Boolean isEmailSent;

    @Column(name = "shipment_charge")
    private Double shipmentCharge;

    @Column(name = "collection_charge")
    private Double collectionCharge;

    @Column(name = "estm_shipment_charge")
    private Double estmShipmentCharge;

    @Column(name = "estm_collection_charge")
    private Double estmCollectionCharge;

    @Column(name = "extra_charge")
    private Double extraCharge;

    @Transient
    private String trackLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id")
    private ShippingOrder shippingOrder;

    public int compareTo(Shipment shipment) {
        if (this.getId() < shipment.getId()) return -1;
        if (this.getId() > shipment.getId()) return 1;
        return 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public BoxSize getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(BoxSize boxSize) {
        this.boxSize = boxSize;
    }

    public Double getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(Double boxWeight) {
        this.boxWeight = boxWeight;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTrackLink() {
        return trackLink;
    }

    public void setTrackLink(String trackLink) {
        this.trackLink = trackLink;
    }

    public Boolean isEmailSent() {
        return isEmailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        isEmailSent = emailSent;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Double getEstmShipmentCharge() {
        return estmShipmentCharge;
    }

    public void setEstmShipmentCharge(Double estmShipmentCharge) {
        this.estmShipmentCharge = estmShipmentCharge;
    }

    public Double getEstmCollectionCharge() {
        return estmCollectionCharge;
    }

    public void setEstmCollectionCharge(Double estmCollectionCharge) {
        this.estmCollectionCharge = estmCollectionCharge;
    }

    public Double getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Double extraCharge) {
        this.extraCharge = extraCharge;
    }

    public Double getShipmentCharge() {
        return shipmentCharge;
    }

    public void setShipmentCharge(Double shipmentCharge) {
        this.shipmentCharge = shipmentCharge;
    }

    public Double getCollectionCharge() {
        return collectionCharge;
    }

    public void setCollectionCharge(Double collectionCharge) {
        this.collectionCharge = collectionCharge;
    }

    public Awb getAwb() {
        if (awb == null) {
            setAwb(new Awb());
        }
        return awb;
    }

    public void setAwb(Awb awb) {
        this.awb = awb;
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}
