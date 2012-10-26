package com.hk.domain.inventory.po;
// Generated Oct 5, 2011 4:39:10 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PurchaseOrder generated by hbm2java
 */
@Entity
@Table (name = "purchase_order")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class PurchaseOrder implements java.io.Serializable {


	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "update_dt", length = 19)
	private Date updateDate;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "purchase_order_status_id", nullable = false)
	private PurchaseOrderStatus purchaseOrderStatus;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "supplier_id", nullable = false)
	private Supplier supplier;

	@Column (name = "po_number")
	private String poNumber;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "created_by", nullable = false)
	private User createdBy;

	@OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "purchaseOrder")
	private List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();

	@Column (name = "payable", precision = 8)
	private Double payable;

	@Column (name = "adv_payment", precision = 8)
	private Double advPayment;

	@Column (name = "payment_details", length = 100)
	private String paymentDetails;

	@Column (name = "reconciled")
	private Boolean reconciled;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "approval_date")
	private Date approvalDate;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "approved_by")
	private User approvedBy;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "po_place_date")
	private Date poPlaceDate;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "est_del_date", length = 19)
	private Date estDelDate;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "est_payment_date", length = 19)
	private Date estPaymentDate;

	@OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "purchaseOrder")
	private List<GoodsReceivedNote> goodsReceivedNotes = new ArrayList<GoodsReceivedNote>();

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "warehouse_id")
	private Warehouse warehouse;

	@Column (name = "taxable_amount")
	private Double taxableAmount;

	@Column (name = "tax_amount")
	private Double taxAmount;

	@Column (name = "surcharge_amount")
	private Double surchargeAmount;

	@Column (name = "discount")
	private Double discount;

	@Column (name = "final_payable_amount")
	private Double finalPayableAmount;

	@Transient
	private int noOfSku;

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

	public PurchaseOrderStatus getPurchaseOrderStatus() {
		return purchaseOrderStatus;
	}

	public void setPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus) {
		this.purchaseOrderStatus = purchaseOrderStatus;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public List<PoLineItem> getPoLineItems() {
		return poLineItems;
	}

	public void setPoLineItems(List<PoLineItem> poLineItems) {
		this.poLineItems = poLineItems;
	}

	public Double getPayable() {
		return payable;
	}

	public void setPayable(Double payable) {
		this.payable = payable;
	}

	public Double getAdvPayment() {
		return advPayment;
	}

	public void setAdvPayment(Double advPayment) {
		this.advPayment = advPayment;
	}

	public String getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(String paymentDetails) {
		this.paymentDetails = paymentDetails;
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

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getPoPlaceDate() {
		return poPlaceDate;
	}

	public void setPoPlaceDate(Date poPlaceDate) {
		this.poPlaceDate = poPlaceDate;
	}

	public Date getEstDelDate() {
		return estDelDate;
	}

	public void setEstDelDate(Date estDelDate) {
		this.estDelDate = estDelDate;
	}

	public Date getEstPaymentDate() {
		return estPaymentDate;
	}

	public void setEstPaymentDate(Date estPaymentDate) {
		this.estPaymentDate = estPaymentDate;
	}

	public List<GoodsReceivedNote> getGoodsReceivedNotes() {
		return goodsReceivedNotes;
	}

	public void setGoodsReceivedNotes(List<GoodsReceivedNote> goodsReceivedNotes) {
		this.goodsReceivedNotes = goodsReceivedNotes;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public int getNoOfSku() {
		return this.poLineItems != null ? this.poLineItems.size() : 0;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Double getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getSurchargeAmount() {
		return surchargeAmount;
	}

	public void setSurchargeAmount(Double surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getFinalPayableAmount() {
		return finalPayableAmount;
	}

	public void setFinalPayableAmount(Double finalPayableAmount) {
		this.finalPayableAmount = finalPayableAmount;
	}
}


