package com.hk.domain.inventory;
// Generated Dec 15, 2011 3:32:41 PM by Hibernate Tools 3.2.4.CR1


import com.akube.framework.gson.JsonSkip;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "est_payment_date")
    private Date estPaymentDate;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false)
	private Date createDate = new Date();

    @Transient
    private boolean selected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "remarks")
    private String remarks;

	@Column(name = "taxable_amount")
	private Double taxableAmount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "surcharge_amount")
	private Double surchargeAmount;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "final_payable_amount")
	private Double finalPayableAmount;


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

	public Date getEstPaymentDate() {
		return estPaymentDate;
	}

	public void setEstPaymentDate(Date estPaymentDate) {
		this.estPaymentDate = estPaymentDate;
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


