package com.hk.domain.courier;
// Generated Aug 15, 2012 1:02:45 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.warehouse.Warehouse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Awb generated by hbm2java
 */
@Entity
@Table(name = "awb", uniqueConstraints = @UniqueConstraint(columnNames = {"courier_id", "awb_number"}))
public class Awb implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awb_status_id", nullable = false)
    private AwbStatus awbStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;


    @Column(name = "awb_number", nullable = false, length = 70)
    private String awbNumber;


    @Column(name = "awb_bar_code", nullable = false, length = 70)
    private String awbBarCode;


    @Column(name = "used", nullable = false)
    private byte used;


    @Column(name = "cod", nullable = false)
    private byte cod;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AwbStatus getAwbStatus() {
        return this.awbStatus;
    }

    public void setAwbStatus(AwbStatus awbStatus) {
        this.awbStatus = awbStatus;
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

    public String getAwbNumber() {
        return this.awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public String getAwbBarCode() {
        return this.awbBarCode;
    }

    public void setAwbBarCode(String awbBarCode) {
        this.awbBarCode = awbBarCode;
    }

    public byte getUsed() {
        return this.used;
    }

    public void setUsed(byte used) {
        this.used = used;
    }

    public byte getCod() {
        return this.cod;
    }

    public void setCod(byte cod) {
        this.cod = cod;
    }


}


