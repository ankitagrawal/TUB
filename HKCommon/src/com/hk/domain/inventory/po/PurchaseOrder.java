package com.hk.domain.inventory.po;
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
import javax.persistence.Transient;

import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

/**
 * PurchaseOrder generated by hbm2java
 */
@Entity
@Table(name = "purchase_order")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class PurchaseOrder implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_status_id", nullable = false)
    private PurchaseOrderStatus purchaseOrderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_date", length = 19)
    private Date invoiceDate;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "po_date", length = 19)
    private Date poDate;

    @Column(name = "po_number")
    private String poNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false, length = 19)
    private Date updateDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "purchaseOrder")
    private List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();

    @Column(name = "payable", precision = 8)
    private Double payable;

    @Column(name = "adv_payment", precision = 8)
    private Double advPayment;

    @Column(name = "payment_details", length = 100)
    private String paymentDetails;

    @Column(name = "reconciled")
    private Boolean reconciled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "est_del_date", length = 19)
    private Date estDelDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "est_payment_date", length = 19)
    private Date estPaymentDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "purchaseOrder")
    private List<GoodsReceivedNote> goodsReceivedNotes = new ArrayList<GoodsReceivedNote>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

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

    public Date getPoDate() {
        return poDate;
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
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
}


