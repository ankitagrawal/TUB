package com.hk.domain.inventory.po;
// Generated Feb 14, 2012 1:22:29 PM by Hibernate Tools 3.2.4.CR1


import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.core.PurchaseFormType;
import com.hk.domain.core.Surcharge;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.payment.PaymentHistory;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PurchaseInvoice generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "purchase_invoice")
public class PurchaseInvoice implements java.io.Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_invoice_status_id")
	private PurchaseInvoiceStatus purchaseInvoiceStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false)
	private Date createDate = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;

	@Column(name = "payment_details")
	private String paymentDetails;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "payment_date")
	private Date paymentDate;

	@Column(name = "taxable_amount")
	private Double taxableAmount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "surcharge_amount")
	private Double surchargeAmount;

	@Column(name = "payable_amount")
	private Double payableAmount;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "reconciled")
	private Boolean reconciled;

	@Column(name = "comments")
	private String comments;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "invoice_date")
	private Date invoiceDate;

	@Column(name = "invoice_number")
	private String invoiceNumber;

	@Column(name = "final_payable_amount")
	private Double finalPayableAmount;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "purchaseInvoice")
	private List<PurchaseInvoiceLineItem> purchaseInvoiceLineItems = new ArrayList<PurchaseInvoiceLineItem>(0);
  
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "purchaseInvoice")
	private List<PaymentHistory> paymentHistories = new ArrayList<PaymentHistory>(0);

	@JsonSkip
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	    name = "purchase_invoice_has_grn",
	    joinColumns = {@JoinColumn(name = "purchase_invoice_id", nullable = false, updatable = false)},
	    inverseJoinColumns = {@JoinColumn(name = "goods_received_note_id", nullable = false, updatable = false)}
	)
	private List<GoodsReceivedNote> goodsReceivedNotes = new ArrayList<GoodsReceivedNote>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "surcharge_id")
	private Surcharge surcharge;

	@Column(name = "freight_forwarding_charges")
	private Double freightForwardingCharges;

	@Column(name = "route_permit_number")
	private String routePermitNumber;

    @Column(name = "reconcilation_date")
    private Date reconcilationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_form_type_id")
    private PurchaseFormType purchaseFormType;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PurchaseInvoiceStatus getPurchaseInvoiceStatus() {
		return this.purchaseInvoiceStatus;
	}

	public void setPurchaseInvoiceStatus(PurchaseInvoiceStatus purchaseInvoiceStatus) {
		this.purchaseInvoiceStatus = purchaseInvoiceStatus;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Supplier getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public User getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public String getPaymentDetails() {
		return this.paymentDetails;
	}

	public void setPaymentDetails(String paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public Date getPaymentDate() {
		return this.paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getTaxableAmount() {
		return this.taxableAmount;
	}

	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public Double getTaxAmount() {
		return this.taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getSurchargeAmount() {
		return this.surchargeAmount;
	}

	public void setSurchargeAmount(Double surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	public Double getPayableAmount() {
		return this.payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Boolean getReconciled() {
		return this.reconciled;
	}

	public void setReconciled(Boolean reconciled) {
		this.reconciled = reconciled;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<PurchaseInvoiceLineItem> getPurchaseInvoiceLineItems() {
		return this.purchaseInvoiceLineItems;
	}

	public void setPurchaseInvoiceLineItems(List<PurchaseInvoiceLineItem> purchaseInvoiceLineItems) {
		this.purchaseInvoiceLineItems = purchaseInvoiceLineItems;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Double getFinalPayableAmount() {
		return finalPayableAmount;
	}

	public void setFinalPayableAmount(Double finalPayableAmount) {
		this.finalPayableAmount = finalPayableAmount;
	}

	public List<GoodsReceivedNote> getGoodsReceivedNotes() {
		return goodsReceivedNotes;
	}

	public void setGoodsReceivedNotes(List<GoodsReceivedNote> goodsReceivedNotes) {
		this.goodsReceivedNotes = goodsReceivedNotes;
	}

	public Surcharge getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(Surcharge surcharge) {
		this.surcharge = surcharge;
	}

	public Double getFreightForwardingCharges() {
		return freightForwardingCharges;
	}

	public void setFreightForwardingCharges(Double freightForwardingCharges) {
		this.freightForwardingCharges = freightForwardingCharges;
	}

	public String getRoutePermitNumber() {
		return routePermitNumber;
	}

	public void setRoutePermitNumber(String routePermitNumber) {
		this.routePermitNumber = routePermitNumber;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

  public Date getReconcilationDate() {
      return reconcilationDate;
  }

  public void setReconcilationDate(Date reconcilationDate) {
      this.reconcilationDate = reconcilationDate;
  }

  public PurchaseFormType getPurchaseFormType() {
      return purchaseFormType;
  }

  public void setPurchaseFormType(PurchaseFormType purchaseFormType) {
      this.purchaseFormType = purchaseFormType;
  }

  public List<PaymentHistory> getPaymentHistories() {
    return paymentHistories;
  }

  public void setPaymentHistories(List<PaymentHistory> paymentHistories) {
    this.paymentHistories = paymentHistories;
  }

  @Override
  public String toString() {
      return id == null ? "" : id.toString();
  }


}


