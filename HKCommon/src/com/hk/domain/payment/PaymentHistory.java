package com.hk.domain.payment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseOrder;

@Entity
@Table(name = "payment_history")
public class PaymentHistory implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long            id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_invoice_id")
    private PurchaseInvoice purchaseInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder   purchaseOrder;

    @Column(name = "amount", precision = 2, scale = 0)
    private Double          amount;

    @Column(name = "mode_of_payment", length = 45)
    private String          modeOfPayment;

    @Temporal(TemporalType.DATE)
    @Column(name = "scheduled_payment_date", length = 10)
    private Date            scheduledPaymentDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "actual_payment_date", length = 10, nullable = true)
    private Date            actualPaymentDate;

    @Column(name = "remarks", length = 100)
    private String          remarks;

    @Column(name = "payment_reference", length = 100)
    private String          paymentReference;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseInvoice getPurchaseInvoice() {
        return this.purchaseInvoice;
    }

    public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getModeOfPayment() {
        return this.modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public Date getScheduledPaymentDate() {
        return this.scheduledPaymentDate;
    }

    public void setScheduledPaymentDate(Date scheduledPaymentDate) {
        this.scheduledPaymentDate = scheduledPaymentDate;
    }

    public Date getActualPaymentDate() {
        return this.actualPaymentDate;
    }

    public void setActualPaymentDate(Date actualPaymentDate) {
        this.actualPaymentDate = actualPaymentDate;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }

}
