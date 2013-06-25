package com.hk.domain.accounting;
// Generated 25 June, 2013 by Tarun Mittal


import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.po.PurchaseInvoice;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table (name = "supplier_transactions")
public class SupplierTransaction implements java.io.Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "supplier_transaction_type_id", nullable = false)
    private SupplierTransactionType supplierTransactionType;

    @Column (name = "date", length = 19)
    private Date date;

    @Column (name = "amount")
    Double amount;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "purchase_invoice_id")
    private PurchaseInvoice purchaseInvoice;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "debit_note_id", nullable = false)
    private DebitNote debitNote;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "credit_note_id", nullable = false)
    private CreditNote creditNote;

    @Column (name = "busy_payment_id", length = 45)
    String busyPaymentId;

    @Column (name = "busy_supplier_balance")
    Double busySupplierBalance;

    @Column (name = "current_balance")
    Double currentBalance;

    @Column (name = "narration")
    String narration;

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

    public SupplierTransactionType getSupplierTransactionType() {
        return supplierTransactionType;
    }

    public void setSupplierTransactionType(SupplierTransactionType supplierTransactionType) {
        this.supplierTransactionType = supplierTransactionType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PurchaseInvoice getPurchaseInvoice() {
        return purchaseInvoice;
    }

    public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }

    public DebitNote getDebitNote() {
        return debitNote;
    }

    public void setDebitNote(DebitNote debitNote) {
        this.debitNote = debitNote;
    }

    public CreditNote getCreditNote() {
        return creditNote;
    }

    public void setCreditNote(CreditNote creditNote) {
        this.creditNote = creditNote;
    }

    public String getBusyPaymentId() {
        return busyPaymentId;
    }

    public void setBusyPaymentId(String busyPaymentId) {
        this.busyPaymentId = busyPaymentId;
    }

    public Double getBusySupplierBalance() {
        return busySupplierBalance;
    }

    public void setBusySupplierBalance(Double busySupplierBalance) {
        this.busySupplierBalance = busySupplierBalance;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}


