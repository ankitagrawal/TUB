package com.hk.domain.hkDelivery;
// Generated Aug 3, 2012 3:17:40 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.user.User;

import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Runsheet generated by hbm2java
 */
@Entity
@Table(name = "runsheet")
public class Runsheet implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    private Date updateDate;


    @Column(name = "expected_collection", nullable = false, precision = 22, scale = 0)
    private double expectedCollection;


    @Column(name = "actual_collection", precision = 22, scale = 0)
    private Double actualCollection;


    @Column(name = "cod_box_count", nullable = false)
    private Long codBoxCount;


    @Column(name = "pre_paid_box_count", nullable = false)
    private Long prePaidBoxCount;


    @Column(name = "remarks", length = 16777215)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User hkDeliveryAgent;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "runsheet_id")
	private List<Consignment> consignments = new ArrayList<Consignment>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runsheet_status_id", nullable = false)
    private RunsheetStatus runsheetStatus;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hub getHub() {
        return this.hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
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

    public double getExpectedCollection() {
        return this.expectedCollection;
    }

    public void setExpectedCollection(double expectedCollection) {
        this.expectedCollection = expectedCollection;
    }

    public Double getActualCollection() {
        return this.actualCollection;
    }

    public void setActualCollection(Double actualCollection) {
        this.actualCollection = actualCollection;
    }

    public Long getCodBoxCount() {
        return this.codBoxCount;
    }

    public void setCodBoxCount(Long codBoxCount) {
        this.codBoxCount = codBoxCount;
    }

    public Long getPrePaidBoxCount() {
        return this.prePaidBoxCount;
    }

    public void setPrePaidBoxCount(Long prePaidBoxCount) {
        this.prePaidBoxCount = prePaidBoxCount;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public User getHkDeliveryAgent() {
        return this.hkDeliveryAgent;
    }

    public void setHkDeliveryAgent(User hkDeliveryAgent) {
        this.hkDeliveryAgent = hkDeliveryAgent;
    }

    public List<Consignment> getConsignments() {
        return consignments;
    }

    public void setConsignments(List<Consignment> consignments) {
        this.consignments = consignments;
    }

    public RunsheetStatus getRunsheetStatus() {
        return runsheetStatus;
    }

    public void setRunsheetStatus(RunsheetStatus runsheetStatus) {
        this.runsheetStatus = runsheetStatus;
    }
}


