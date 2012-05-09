package com.hk.domain.courier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.warehouse.Warehouse;

@Entity
@Table(name = "courier_pricing_engine")
public class CourierPricingEngine implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long      id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier   courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "first_base_wt", nullable = false, precision = 6)
    private Double    firstBaseWt;

    @Column(name = "first_base_cost", nullable = false, precision = 6)
    private Double    firstBaseCost;

    @Column(name = "second_base_wt", precision = 6)
    private Double    secondBaseWt;

    @Column(name = "second_base_cost", precision = 6)
    private Double    secondBaseCost;

    @Column(name = "additional_wt", nullable = false, precision = 6)
    private Double    additionalWt;

    @Column(name = "additional_cost", nullable = false, precision = 6)
    private Double    additionalCost;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Courier getCourier() {
        return this.courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Double getFirstBaseWt() {
        return this.firstBaseWt;
    }

    public void setFirstBaseWt(Double firstBaseWt) {
        this.firstBaseWt = firstBaseWt;
    }

    public Double getFirstBaseCost() {
        return this.firstBaseCost;
    }

    public void setFirstBaseCost(Double firstBaseCost) {
        this.firstBaseCost = firstBaseCost;
    }

    public Double getSecondBaseWt() {
        return this.secondBaseWt;
    }

    public void setSecondBaseWt(Double secondBaseWt) {
        this.secondBaseWt = secondBaseWt;
    }

    public Double getSecondBaseCost() {
        return this.secondBaseCost;
    }

    public void setSecondBaseCost(Double secondBaseCost) {
        this.secondBaseCost = secondBaseCost;
    }

    public Double getAdditionalWt() {
        return this.additionalWt;
    }

    public void setAdditionalWt(Double additionalWt) {
        this.additionalWt = additionalWt;
    }

    public Double getAdditionalCost() {
        return this.additionalCost;
    }

    public void setAdditionalCost(Double additionalCost) {
        this.additionalCost = additionalCost;
    }

}
