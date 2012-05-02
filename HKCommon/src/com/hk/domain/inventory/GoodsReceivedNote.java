package com.hk.domain.inventory;
// Generated Dec 15, 2011 3:32:41 PM by Hibernate Tools 3.2.4.CR1


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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;


@SuppressWarnings("serial")
@Entity
@Table(name = "goods_received_note")
public class GoodsReceivedNote implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "grn_status_id", nullable = false)
  private GrnStatus grnStatus;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "grn_date", nullable = false, length = 19)
  private Date grnDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchase_order_id", nullable = false)
  private PurchaseOrder purchaseOrder;

  @Column(name = "payable", precision = 12)
  private Double payable;

  @Column(name = "payment_details", length = 100)
  private String paymentDetails;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "payment_date", length = 19)
  private Date paymentDate;

  @Column(name = "invoice_number", length = 100)
  private String invoiceNumber;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "invoice_date", length = 19)
  private Date invoiceDate;

  @Column(name = "reconciled")
  private Boolean reconciled;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "received_by")
  private User receivedBy;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goodsReceivedNote")
  private List<GrnLineItem> grnLineItems = new ArrayList<GrnLineItem>(0);

	@JsonSkip
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "purchase_invoice_has_grn",
      joinColumns = {@JoinColumn(name = "goods_received_note_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "purchase_invoice_id", nullable = false, updatable = false)}
  )
  private List<PurchaseInvoice> purchaseInvoices = new ArrayList<PurchaseInvoice>();

	@Transient
	private boolean selected;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GrnStatus getGrnStatus() {
    return this.grnStatus;
  }

  public void setGrnStatus(GrnStatus grnStatus) {
    this.grnStatus = grnStatus;
  }

  public Date getGrnDate() {
    return this.grnDate;
  }

  public void setGrnDate(Date grnDate) {
    this.grnDate = grnDate;
  }

  public PurchaseOrder getPurchaseOrder() {
    return purchaseOrder;
  }

  public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
    this.purchaseOrder = purchaseOrder;
  }

  public Double getPayable() {
    return this.payable;
  }

  public void setPayable(Double payable) {
    this.payable = payable;
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

  public List<GrnLineItem> getGrnLineItems() {
    return this.grnLineItems;
  }

  public void setGrnLineItems(List<GrnLineItem> grnLineItems) {
    this.grnLineItems = grnLineItems;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public Date getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(Date invoiceDate) {
    this.invoiceDate = invoiceDate;
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

  public User getReceivedBy() {
    return receivedBy;
  }

  public void setReceivedBy(User receivedBy) {
    this.receivedBy = receivedBy;
  }

	public boolean isSelected() {
		return selected;
	}

	public boolean getSelected(){
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public List<PurchaseInvoice> getPurchaseInvoices() {
		return purchaseInvoices;
	}

	public void setPurchaseInvoices(List<PurchaseInvoice> purchaseInvoices) {
		this.purchaseInvoices = purchaseInvoices;
	}

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }
}


