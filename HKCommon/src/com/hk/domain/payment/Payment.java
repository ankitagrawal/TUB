package com.hk.domain.payment;
// Generated 25 Mar, 2011 11:57:39 AM by Hibernate Tools 3.2.4.CR1


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
import javax.persistence.Transient;

import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;

/**
 * Payment generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "payment")
public class Payment implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_mode_id", nullable = false)
  private PaymentMode paymentMode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_status_id", nullable = false)
  private PaymentStatus paymentStatus;

  @Column(name = "bank_code")
  private String bankCode;

  @Column(name = "amount", nullable = false, precision = 8)
  private Double amount;

  @Column(name = "gateway_order_id", length = 30)
  private String gatewayOrderId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "payment_date", length = 19)
  private Date paymentDate;

  @Column(name = "gateway_reference_id", length = 45)
  private String gatewayReferenceId;

  @Column(name = "ip", length = 15)
  private String ip;

  @Column(name = "payment_checksum", length = 45)
  private String paymentChecksum;

  @Column(name = "error_log", length = 45)
  private String errorLog;

  @Column(name = "email", length = 80)
  private String email;

  @Column(name = "billing_address_actual", length = 65535)
  private String billingAddressActual;

  @Column(name = "contact_name", length = 80)
  private String contactName;

  @Column(name = "contact_number", length = 25)
  private String contactNumber;

  @Column(name = "bank_name", length = 45)
  private String bankName;

  @Column(name = "bank_branch", length = 45)
  private String bankBranch;

  @Column(name = "bank_city", length = 45)
  private String bankCity;

  @Column(name = "cheque_number", length = 10)
  private String chequeNumber;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @Transient
  private boolean selected;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PaymentMode getPaymentMode() {
    return this.paymentMode;
  }

  public void setPaymentMode(PaymentMode paymentMode) {
    this.paymentMode = paymentMode;
  }

  public Order getOrder() {
    return this.order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public PaymentStatus getPaymentStatus() {
    return this.paymentStatus;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public Double getAmount() {
    return this.amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getGatewayOrderId() {
    return this.gatewayOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public Date getPaymentDate() {
    return this.paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public String getGatewayReferenceId() {
    return this.gatewayReferenceId;
  }

  public void setGatewayReferenceId(String gatewayReferenceId) {
    this.gatewayReferenceId = gatewayReferenceId;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getPaymentChecksum() {
    return this.paymentChecksum;
  }

  public void setPaymentChecksum(String paymentChecksum) {
    this.paymentChecksum = paymentChecksum;
  }

  public String getErrorLog() {
    return this.errorLog;
  }

  public void setErrorLog(String errorLog) {
    this.errorLog = errorLog;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBillingAddressActual() {
    return this.billingAddressActual;
  }

  public void setBillingAddressActual(String billingAddressActual) {
    this.billingAddressActual = billingAddressActual;
  }

  public String getContactName() {
    return this.contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactNumber() {
    return this.contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getBankName() {
    return this.bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankBranch() {
    return this.bankBranch;
  }

  public void setBankBranch(String bankBranch) {
    this.bankBranch = bankBranch;
  }

  public String getBankCity() {
    return this.bankCity;
  }

  public void setBankCity(String bankCity) {
    this.bankCity = bankCity;
  }

  public String getChequeNumber() {
    return this.chequeNumber;
  }

  public void setChequeNumber(String chequeNumber) {
    this.chequeNumber = chequeNumber;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  @Transient
  public boolean isCODPayment(){
   // return getPaymentMode().getId().equals(EnumPaymentMode.COD.getId());
      //TODO:rewrite
      return true;
  }

  
  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
    public String getPaymentDetails() {
      String paymentStatusString = paymentStatus != null ? paymentStatus.getName() : "";
      String paymentModeString = paymentMode != null ? paymentMode.getName() : "";
    return "Payment Details{" +
        "id=" + id +
        ", paymentAmount=" + amount +
        ", paymentGatewayOrderId=" + gatewayOrderId  +
        ", paymentStatus=" + paymentStatusString +
        ", paymentMode=" + paymentModeString +
        '}';
  }

}


