package com.hk.domain.inventory;
// Generated 10 Jul, 2012 10:14:48 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.cycleCount.CycleCount;
import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;
import java.util.Date;

/**
 * BrandsToAudit generated by hbm2java
 */
@Entity
@Table(name = "brands_to_audit")
public class BrandsToAudit implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_user_id", nullable = false)
    private User auditor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "brand", nullable = false, length = 45)
    private String brand;

	@Temporal (TemporalType.DATE)
    @Column(name = "audit_date", nullable = false, length = 10)
    private Date auditDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_dt", length = 10)
    private Date updateDate;

    @Column(name = "audit_status", nullable = false)
    private Long auditStatus;

    public void setId(Long id) {
        this.id = id;
    }

	@JsonSkip
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "brandsToAudit")
	private CycleCount cycleCount;
	

	public Long getId() {
		return this.id;
	}

    public User getAuditor() {
        return auditor;
    }

    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getAuditDate() {
        return this.auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Date getUpdateDate(){
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate){
        this.updateDate = updateDate;
    }
    public Long getAuditStatus() {
        return this.auditStatus;
    }

    public void setAuditStatus(Long auditStatus) {
        this.auditStatus = auditStatus;
    }

	@Override
	public String toString() {
		return id != null ? id.toString() : "";
	}

	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BrandsToAudit))
            return false;

        BrandsToAudit brandsToAudit = (BrandsToAudit) o;

        if (this.id != null && brandsToAudit.getId() != null){
            return this.id.equals(brandsToAudit.getId());
        }
        return false;
    }

	public CycleCount getCycleCount() {
		return cycleCount;
	}

	public void setCycleCount(CycleCount cycleCount) {
		this.cycleCount = cycleCount;
	}
}


