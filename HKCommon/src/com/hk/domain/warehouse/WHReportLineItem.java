package com.hk.domain.warehouse;

import com.hk.domain.shippingOrder.LineItem;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vidur Malhotra
 * Date: 8/30/13
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "wh_report_line_item")
public class WHReportLineItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_item_id", unique = true, nullable = false)
    LineItem lineItem;

    @Column(name = "shipment_charge")
    Double shipmentCharge;

    @Column(name = "collection_charge")
    Double collectionCharge;

    @Column(name = "estm_shipment_charge")
    Double estmShipmentCharge;

    @Column(name = "estm_collection_charge")
    Double estmCollectionCharge;

    @Column(name = "extra_charge")
    Double extraCharge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
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
}
