package com.hk.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/11/12
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "order_payment_reconciliation")
public class OrderPaymentReconciliation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "base_order_id", nullable = false)
	private Order baseOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipping_order_id")
	private ShippingOrder shippingOrder;

	@Column(name = "payment_process_type")
	private String paymentProcessType;

	@Column(name = "reconciled", nullable = false)
	private boolean reconciled;

	@Column(name = "reconciled_amount", nullable = false)
	private Double reconciledAmount;

	@Column(name = "reward_points_against_cancellation", nullable = false)
	private Double rewardPointsAgainstCancellation = 0D;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getBaseOrder() {
		return baseOrder;
	}

	public void setBaseOrder(Order baseOrder) {
		this.baseOrder = baseOrder;
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public String getPaymentProcessType() {
		return paymentProcessType;
	}

	public void setPaymentProcessType(String paymentProcessType) {
		this.paymentProcessType = paymentProcessType;
	}

	public boolean isReconciled() {
		return reconciled;
	}

	public void setReconciled(boolean reconciled) {
		this.reconciled = reconciled;
	}

	public Double getReconciledAmount() {
		return reconciledAmount;
	}

	public void setReconciledAmount(Double reconciledAmount) {
		this.reconciledAmount = reconciledAmount;
	}

	public Double getRewardPointsAgainstCancellation() {
		return rewardPointsAgainstCancellation;
	}

	public void setRewardPointsAgainstCancellation(Double rewardPointsAgainstCancellation) {
		this.rewardPointsAgainstCancellation = rewardPointsAgainstCancellation;
	}
}
