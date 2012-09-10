package com.hk.domain.courier;

// Generated 27 Mar, 2012 6:51:31 AM by Hibernate Tools 3.2.4.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.core.Pincode;
import com.hk.domain.warehouse.Warehouse;

/**
 * PincodeDefaultCourier generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pincode_default_courier")
public class PincodeDefaultCourier implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long      id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier   Courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", nullable = false)
    private Pincode   pincode;

    @Column(name = "cod")
    private boolean              cod;

     @Column(name = "ground_shipping")
    private boolean              groundShipping;

    @Column(name = "estimated_shipping_cost")
    private Double    estimatedShippingCost;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Courier getCourier() {
        return Courier;
    }

    public void setCourier(Courier courier) {
        Courier = courier;
    }

    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public boolean isGroundShipping() {
        return groundShipping;
    }

    public void setGroundShipping(boolean groundShipping) {
        this.groundShipping = groundShipping;
    }

    public Double getEstimatedShippingCost() {
        return estimatedShippingCost;
    }

    public void setEstimatedShippingCost(Double estimatedShippingCost) {
        this.estimatedShippingCost = estimatedShippingCost;
    }
}
