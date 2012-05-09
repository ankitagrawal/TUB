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

import com.hk.domain.core.Pincode;
import com.hk.domain.warehouse.Warehouse;

@Entity
@Table(name = "pincode_region_zone")
public class PincodeRegionZone implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long         id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_type_id", nullable = false)
    private RegionType   regionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", nullable = false)
    private Pincode      pincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_group_id", nullable = false)
    private CourierGroup courierGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse    warehouse;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegionType getRegionType() {
        return this.regionType;
    }

    public void setRegionType(RegionType regionType) {
        this.regionType = regionType;
    }

    public Pincode getPincode() {
        return this.pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public CourierGroup getCourierGroup() {
        return courierGroup;
    }

    public void setCourierGroup(CourierGroup courierGroup) {
        this.courierGroup = courierGroup;
    }

    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

}
