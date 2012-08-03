package com.hk.domain;
// Generated Aug 3, 2012 3:17:40 PM by Hibernate Tools 3.2.4.CR1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PaymentReconciliation generated by hbm2java
 */
@Entity
@Table(name="payment_reconciliation")
public class PaymentReconciliation  implements java.io.Serializable {


 
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)
    private Long id;
 
    
    @Column(name="amount", nullable=false, precision=22, scale=0)
    private double amount;
 
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date", length=19)
    private Date createDate;
 
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_date", length=19)
    private Date updateDate;
 
    
    @Column(name="user_id")
    private Long userId;
 
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="paymentReconciliation")
    private Set<PaymentHasConsignment> paymentHasConsignments = new HashSet<PaymentHasConsignment>(0);
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return this.updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Long getUserId() {
        return this.userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Set<PaymentHasConsignment> getPaymentHasConsignments() {
        return this.paymentHasConsignments;
    }
    
    public void setPaymentHasConsignments(Set<PaymentHasConsignment> paymentHasConsignments) {
        this.paymentHasConsignments = paymentHasConsignments;
    }




}


