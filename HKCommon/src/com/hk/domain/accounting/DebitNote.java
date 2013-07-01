package com.hk.domain.accounting;
// Generated 30 Dec, 2011 7:02:04 PM by Hibernate Tools 3.2.4.CR1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.catalog.Supplier;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.warehouse.Warehouse;

@SuppressWarnings("serial")
@Entity
@Table (name = "debit_note")
public class DebitNote implements java.io.Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "goods_received_note_id", nullable = true)
    private GoodsReceivedNote goodsReceivedNote;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "debit_note_status_id", nullable = false)
    private DebitNoteStatus debitNoteStatus;

    @Temporal (TemporalType.TIMESTAMP)
    @Column (name = "create_date", nullable = false, length = 19)
    private Date createDate;

    @Column (name = "is_debit_to_supplier")
    private Boolean isDebitToSupplier;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "remarks")
    private String remarks;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "debitNote")
    private Set<DebitNoteLineItem> debitNoteLineItems = new HashSet<DebitNoteLineItem>(0);
    
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "debit_note_type_id", nullable = false)
    private DebitNoteType debitNoteType;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="purchase_invoice_id")
    private PurchaseInvoice purchaseInvoice;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reconciliation_voucher_id")
    private ReconciliationVoucher reconciliationVoucher;

    @Column(name = "debit_note_number")
    private String debitNoteNumber;
    
    @Column(name="email_sent_to_vendor")
    private Boolean emailSentToVendor;
    
    @Column(name="freight_forwarding_charges")
    private Double freightForwardingCharges;
    
    @Column(name="final_debit_amount")
    private Double finalDebitAmount;
    
    @Temporal (TemporalType.TIMESTAMP)
    @Column (name = "close_date", length = 19)
    private Date closeDate;
    
    @Column(name = "discount")
	private Double discount;
    
    @Column(name = "taxable_amount")
	private Double taxableAmount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "surcharge_amount")
	private Double surchargeAmount;

	@Column(name = "payable_amount")
	private Double payableAmount;
	
	 @Column (name = "destination_address")
	  private String destinationAddress;
	
	@ManyToOne
	  @JoinColumn(name = "courier_pickup_detail_id")
	  private CourierPickupDetail courierPickupDetail;
	
	 @Column(name="return_by_hand")
	  private Boolean returnByHand;
	 
	 @Temporal (TemporalType.TIMESTAMP)
	  @Column(name="return_date")
	  private Date returnDate;
	  
	  @Column (name = "return_address")
	  private String returnAddress;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public GoodsReceivedNote getGoodsReceivedNote() {
        return this.goodsReceivedNote;
    }

    public void setGoodsReceivedNote(GoodsReceivedNote goodsReceivedNote) {
        this.goodsReceivedNote = goodsReceivedNote;
    }

    public DebitNoteStatus getDebitNoteStatus() {
        return this.debitNoteStatus;
    }

    public void setDebitNoteStatus(DebitNoteStatus debitNoteStatus) {
        this.debitNoteStatus = debitNoteStatus;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsDebitToSupplier() {
        return this.isDebitToSupplier;
    }

    public void setIsDebitToSupplier(Boolean isDebitToSupplier) {
        this.isDebitToSupplier = isDebitToSupplier;
    }

    public Set<DebitNoteLineItem> getDebitNoteLineItems() {
        return this.debitNoteLineItems;
    }

    public void setDebitNoteLineItems(Set<DebitNoteLineItem> debitNoteLineItems) {
        this.debitNoteLineItems = debitNoteLineItems;
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

	public DebitNoteType getDebitNoteType() {
		return debitNoteType;
	}

	public void setDebitNoteType(DebitNoteType debitNoteType) {
		this.debitNoteType = debitNoteType;
	}

	public PurchaseInvoice getPurchaseInvoice() {
		return purchaseInvoice;
	}

	public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

	public ReconciliationVoucher getReconciliationVoucher() {
		return reconciliationVoucher;
	}

	public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
		this.reconciliationVoucher = reconciliationVoucher;
	}

    public String getDebitNoteNumber() {
        return debitNoteNumber;
    }

    public void setDebitNoteNumber(String debitNoteNumber) {
        this.debitNoteNumber = debitNoteNumber;
    }
    public Boolean getEmailSentToVendor() {
		return emailSentToVendor;
	}
	
	public void setEmailSentToVendor(Boolean emailSentToVendor) {
		this.emailSentToVendor = emailSentToVendor;
	}

	public Double getFreightForwardingCharges() {
		return freightForwardingCharges;
	}

	public void setFreightForwardingCharges(Double freightForwardingCharges) {
		this.freightForwardingCharges = freightForwardingCharges;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Double getFinalDebitAmount() {
		return finalDebitAmount;
	}

	public void setFinalDebitAmount(Double finalDebitAmount) {
		this.finalDebitAmount = finalDebitAmount;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public CourierPickupDetail getCourierPickupDetail() {
		return courierPickupDetail;
	}

	public void setCourierPickupDetail(CourierPickupDetail courierPickupDetail) {
		this.courierPickupDetail = courierPickupDetail;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public Boolean getReturnByHand() {
		return returnByHand;
	}

	public void setReturnByHand(Boolean returnByHand) {
		this.returnByHand = returnByHand;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}
}


