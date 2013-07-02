package com.hk.domain.payment;

// Generated 25 Mar, 2011 11:57:39 AM by Hibernate Tools 3.2.4.CR1

import com.akube.framework.gson.JsonSkip;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.BillingAddress;

import javax.persistence.*;
import java.util.Date;

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

	@JsonSkip
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gateway_id")
	private Gateway gateway;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issuer_id")
	private Issuer issuer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_status_id", nullable = false)
	private PaymentStatus paymentStatus;

	@Column(name = "amount", nullable = false, precision = 8)
	private Double amount;

	@Column(name = "gateway_order_id", length = 30)
	private String gatewayOrderId;

	@JsonSkip
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "payment_date", length = 19)
	private Date paymentDate;

	@Column(name = "gateway_reference_id", length = 45)
	private String gatewayReferenceId;

	@Column(name = "ip", length = 15)
	private String ip;

	@Column(name = "payment_checksum", length = 45)
	private String paymentChecksum;

	@Column(name = "error_log", length = 100)
	private String errorLog;

	@Column(name = "email", length = 80)
	private String email;

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

	@Column(name = "rrn", length = 45)
	private String rrn;

	@Column(name = "response_message", length = 200)
	private String responseMessage;

	@Column(name = "authid_code", length = 45)
	private String authIdCode;

	@JsonSkip
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_address_id")
	private BillingAddress billingAddress;

	@Column(name = "last_four_digit_card_no")
	private Long lastFourDigitCardNo;

    @Column(name = "transaction_type")
    private String transactionType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Payment parent;

    @Column(name = "refund_amount", nullable = false, precision = 8)
    private Double refundAmount;

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

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getAuthIdCode() {
		return authIdCode;
	}

	public void setAuthIdCode(String authIdCode) {
		this.authIdCode = authIdCode;
	}

	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Transient
	public boolean isCODPayment() {
		return getPaymentMode().getId().equals(EnumPaymentMode.COD.getId());
	}

	@Override
	public String toString() {
		return id == null ? "" : id.toString();
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public Issuer getIssuer() {
		return issuer;
	}

	public void setIssuer(Issuer issuer) {
		this.issuer = issuer;
	}

	public Long getLastFourDigitCardNo() {
		return lastFourDigitCardNo;
	}

	public void setLastFourDigitCardNo(Long lastFourDigitCardNo) {
		this.lastFourDigitCardNo = lastFourDigitCardNo;
	}

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Payment getParent() {
        return parent;
    }

    public void setParent(Payment parent) {
        this.parent = parent;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getPaymentDetails() {
		String paymentStatusString = paymentStatus != null ? paymentStatus.getName() : "";
		String paymentModeString = paymentMode != null ? paymentMode.getName() : "";
		return "Payment Details{" + "id=" + id + ", paymentAmount=" + amount + ", paymentGatewayOrderId=" + gatewayOrderId + ", paymentStatus=" + paymentStatusString
				+ ", paymentMode=" + paymentModeString + '}';
	}

}
