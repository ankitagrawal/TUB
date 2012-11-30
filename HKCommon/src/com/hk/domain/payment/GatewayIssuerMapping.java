package com.hk.domain.payment;

import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/21/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "gateway_issuer_mapping")
public class GatewayIssuerMapping implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gateway_id", nullable = false)
    private Gateway gateway;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issuer_id", nullable = false)
    private Issuer issuer;

    @Column(name = "reconciliation_charge", nullable = false, scale = 0)
    private Double reconciliationCharge;

    @Column(name = "priority", scale = 0)
    private Long priority;

    @Column(name = "issuer_code")
    private String issuerCode;

    @Column(name = "active", scale = 0)
    private Boolean active;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "downtime_start_date", length = 19)
    private Date downtimeStartDate;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "downtime_end_date", length = 19)
    private Date downtimeEndDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDowntimeStartDate() {
        return downtimeStartDate;
    }

    public void setDowntimeStartDate(Date downtimeStartDate) {
        this.downtimeStartDate = downtimeStartDate;
    }

    public Date getDowntimeEndDate() {
        return downtimeEndDate;
    }

    public void setDowntimeEndDate(Date downtimeEndDate) {
        this.downtimeEndDate = downtimeEndDate;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(String issuerCode) {
        this.issuerCode = issuerCode;
    }

    public Double getReconciliationCharge() {
        return reconciliationCharge;
    }

    public void setReconciliationCharge(Double reconciliationCharge) {
        this.reconciliationCharge = reconciliationCharge;
    }
}
