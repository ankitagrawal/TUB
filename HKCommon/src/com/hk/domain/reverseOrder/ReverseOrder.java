package com.hk.domain.reverseOrder;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.user.User;
import com.hk.domain.inventory.rv.ReconciliationStatus;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA. * User: Neha * Date: Feb 6, 2013 * Time: 1:07:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReverseOrder {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long                        id;

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id")
    private ShippingOrder shippingOrder;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courier_pickup_detail_id")
	private CourierPickupDetail courierPickupDetail;

	@Column (name = "amount", nullable = false)
	private Double amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column (name = "action_proposed")
	private String actionProposed;

	@Column (name = "reconciled")
	private ReconciliationStatus reconciliationStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public CourierPickupDetail getCourierPickupDetail() {
		return courierPickupDetail;
	}

	public void setCourierPickupDetail(CourierPickupDetail courierPickupDetail) {
		this.courierPickupDetail = courierPickupDetail;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getActionProposed() {
		return actionProposed;
	}

	public void setActionProposed(String actionProposed) {
		this.actionProposed = actionProposed;
	}

	public ReconciliationStatus getReconciliationStatus() {
		return reconciliationStatus;
	}

	public void setReconciliationStatus(ReconciliationStatus reconciliationStatus) {
		this.reconciliationStatus = reconciliationStatus;
	}
}
