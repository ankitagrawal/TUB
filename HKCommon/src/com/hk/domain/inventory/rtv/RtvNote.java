package com.hk.domain.inventory.rtv;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.user.User;

import javax.persistence.*;

import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.inventory.po.PurchaseInvoice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 6:14:52 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "rtv_note")

@NamedQueries({
    @NamedQuery(name = "getRtvNoteByExtraInventory" , query = "select rtv from RtvNote rtv where extraInventory.id = :extraInventoryId"),
    @NamedQuery(name = "getRtvNoteById", query = "select rtv from RtvNote rtv where id = :rtvNoteId")
})

public class RtvNote implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column (name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne  (fetch = FetchType.LAZY)
  @JoinColumn (name = "extra_inventory_id" , nullable = false)
  private ExtraInventory extraInventory;

  @ManyToOne
  @JoinColumn (name = "rtv_note_status_id", nullable = false)
  private RtvNoteStatus rtvNoteStatus;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "user_id", nullable = false)
  private User createdBy;

  @Column (name = "is_debit_to_supplier")
  private Boolean isDebitToSupplier;

  @Column (name = "reconciled")
  private Boolean reconciled;

  @Column (name = "remarks")
  private String remarks;

  @Column (name = "destination_address")
  private String destinationAddress;

  @ManyToOne
  @JoinColumn(name = "courier_pickup_detail_id")
  private CourierPickupDetail courierPickupDetail;

  @Temporal (TemporalType.TIMESTAMP)
  @Column (name = "create_dt", nullable = false, length = 19)
  private Date createDate = new Date();

  @Temporal (TemporalType.TIMESTAMP)
  @Column (name = "update_dt", nullable = false, length = 19)
  private Date updateDate = new Date();

  @JsonSkip
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "purchase_invoice_has_rtv_note",
          joinColumns = {@JoinColumn(name = "rtv_note_id", nullable = false, updatable = false)},
          inverseJoinColumns = {@JoinColumn(name = "purchase_invoice_id", nullable = false, updatable = false)}
  )
  private List<PurchaseInvoice> purchaseInvoices = new ArrayList<PurchaseInvoice>();
  
  @Column(name="shipping_charge_hk")
  private Double shippingChargeHk;
  
  @Column(name="shipping_charge_vendor")
  private Double shippingChargeVendor;
  
  @Column(name="return_by_hand")
  private Boolean returnByHand;
  
  @Temporal (TemporalType.TIMESTAMP)
  @Column(name="rtv_return_date")
  private Date rtvReturnDate;
  
  @Column (name = "rtv_return_address")
  private String rtvReturnAddress;
  
  @Column (name = "rtv_tracking_number")
  private String rtvTrackingNumber;
  
  
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ExtraInventory getExtraInventory() {
    return extraInventory;
  }

  public void setExtraInventory(ExtraInventory extraInventory) {
    this.extraInventory = extraInventory;
  }

  public RtvNoteStatus getRtvNoteStatus() {
    return rtvNoteStatus;
  }

  public void setRtvNoteStatus(RtvNoteStatus rtvNoteStatus) {
    this.rtvNoteStatus = rtvNoteStatus;
  }

  public Boolean isDebitToSupplier() {
    return isDebitToSupplier;
  }

  public Boolean getDebitToSupplier() {
    return isDebitToSupplier;
  }

  public void setDebitToSupplier(Boolean debitToSupplier) {
    isDebitToSupplier = debitToSupplier;
  }

  public Boolean isReconciled() {
    return reconciled;
  }

  public Boolean getReconciled() {
    return reconciled;
  }

  public void setReconciled(Boolean reconciled) {
    this.reconciled = reconciled;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public String getDestinationAddress() {
    return destinationAddress;
  }

  public void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
  }

  public CourierPickupDetail getCourierPickupDetail() {
    return courierPickupDetail;
  }

  public void setCourierPickupDetail(CourierPickupDetail courierPickupDetail) {
    this.courierPickupDetail = courierPickupDetail;
  }

  public List<PurchaseInvoice> getPurchaseInvoices() {
	return purchaseInvoices;
  }

  public void setPurchaseInvoices(List<PurchaseInvoice> purchaseInvoices) {
	this.purchaseInvoices = purchaseInvoices;
  }

	public Double getShippingChargeHk() {
		return shippingChargeHk;
	}
	
	public void setShippingChargeHk(Double shippingChargeHk) {
		this.shippingChargeHk = shippingChargeHk;
	}
	
	public Double getShippingChargeVendor() {
		return shippingChargeVendor;
	}
	
	public void setShippingChargeVendor(Double shippingChargeVendor) {
		this.shippingChargeVendor = shippingChargeVendor;
	}

	public Boolean getIsDebitToSupplier() {
		return isDebitToSupplier;
	}

	public void setIsDebitToSupplier(Boolean isDebitToSupplier) {
		this.isDebitToSupplier = isDebitToSupplier;
	}

	public Boolean getReturnByHand() {
		return returnByHand;
	}

	public void setReturnByHand(Boolean returnByHand) {
		this.returnByHand = returnByHand;
	}

	public Date getRtvReturnDate() {
		return rtvReturnDate;
	}

	public void setRtvReturnDate(Date rtvReturnDate) {
		this.rtvReturnDate = rtvReturnDate;
	}

	public String getRtvReturnAddress() {
		return rtvReturnAddress;
	}

	public void setRtvReturnAddress(String rtvReturnAddress) {
		this.rtvReturnAddress = rtvReturnAddress;
	}

	public String getRtvTrackingNumber() {
		return rtvTrackingNumber;
	}

	public void setRtvTrackingNumber(String rtvTrackingNumber) {
		this.rtvTrackingNumber = rtvTrackingNumber;
	}
	
}
