package com.hk.domain.payment;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="payment_history")
public class PaymentHistory  implements java.io.Serializable {


 
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)
    private Long id;
 
    
    @Column(name="purchase_invoice_id")
    private Long purchaseInvoiceId;
 
    
    @Column(name="purchase_order_id", nullable=false)
    private Long purchaseOrderId;
 
    
    @Column(name="amount", precision=2, scale=0)
    private Double amount;
 
    
    @Column(name="mode_of_payment", length=45)
    private String modeOfPayment;
 
    @Temporal(TemporalType.DATE)
    @Column(name="scheduled_payment_date", length=10)
    private Date scheduledPaymentDate;
 
    @Temporal(TemporalType.DATE)
    @Column(name="actual_payment_date", length=10)
    private Date actualPaymentDate;
 
    
    @Column(name="remarks", length=100)
    private String remarks;
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public Long getPurchaseInvoiceId() {
        return this.purchaseInvoiceId;
    }
    
    public void setPurchaseInvoiceId(Long purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }
    public Long getPurchaseOrderId() {
        return this.purchaseOrderId;
    }
    
    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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




}
