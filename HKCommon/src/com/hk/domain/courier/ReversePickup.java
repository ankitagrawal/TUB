package com.hk.domain.courier;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 4, 2012
 * Time: 2:17:08 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings ("serial")
@Entity
@Table(name = "reverse_pickup")
public class ReversePickup {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long                        id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id")
    private ShippingOrder shippingOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

	@Column(name = "pickup_confirmation_no")
	private String pickupConfirmationNo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pickup_date", nullable = false, length = 19)
    private Date pickupDate;

	@ManyToOne
    @Column(name = "pickup_status_id", nullable = false)
	private PickupStatus pickupStatus;

	@ManyToOne
	@JoinColumn(name = "reconciliation_status_id", nullable = false)
    private ReconciliationStatus reconciliationStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
    private User user;

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

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public String getPickupConfirmationNo() {
		return pickupConfirmationNo;
	}

	public void setPickupConfirmationNo(String pickupConfirmationNo) {
		this.pickupConfirmationNo = pickupConfirmationNo;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public PickupStatus getPickupStatus() {
		return pickupStatus;
	}

	public void setPickupStatus(PickupStatus pickupStatus) {
		this.pickupStatus = pickupStatus;
	}

	public ReconciliationStatus getReconciliationStatus() {
		return reconciliationStatus;
	}

	public void setReconciliationStatus(ReconciliationStatus reconciliationStatus) {
		this.reconciliationStatus = reconciliationStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
