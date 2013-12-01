package com.hk.domain.inventory.rv;
// Generated Oct 5, 2011 4:39:10 PM by Hibernate Tools 3.2.4.CR1


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.catalog.Supplier;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.akube.framework.gson.JsonSkip;


@Entity
@Table(name = "reconciliation_voucher")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class ReconciliationVoucher implements java.io.Serializable {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "reconciliation_date", length = 19)
  private Date reconciliationDate;

  @Column(name = "remarks")
  private String remarks;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @JsonSkip
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reconciliationVoucher")
  private List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="reconciliation_type_id")
  private ReconciliationType reconciliationType;
  
  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "supplier_id")
  private Supplier supplier;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getReconciliationDate() {
    return reconciliationDate;
  }

  public void setReconciliationDate(Date reconciliationDate) {
    this.reconciliationDate = reconciliationDate;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public List<RvLineItem> getRvLineItems() {
    return rvLineItems;
  }

  public void setRvLineItems(List<RvLineItem> rvLineItems) {
    this.rvLineItems = rvLineItems;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }


	public ReconciliationType getReconciliationType() {
		return reconciliationType;
	}

    public void setReconciliationType(ReconciliationType reconciliationType) {
        this.reconciliationType = reconciliationType;
    }
    
    public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Override
    public String toString() {
        return this.id != null ? this.id.toString() : "";
    }

}