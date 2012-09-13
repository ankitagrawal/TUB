package com.hk.domain.order;

import com.hk.domain.core.PaymentMode;

import javax.persistence.*;

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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "base_order_id", nullable = false)
	private Order baseOrder;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipping_order_id")
	private ShippingOrder shippingOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_mode_id", nullable = false)
	private PaymentMode paymentMode;

	@Column(name = "reconciled", nullable = false)
	private boolean reconciled;

	@Column(name = "reconciled_amount", nullable = false)
	private Double reconciledAmount;

	@Column(name = "reward_points_against_cancellation")
	private Double rewardPointsAgainstCancellation;

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

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
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
