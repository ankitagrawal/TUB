package com.hk.domain.reverseOrder;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.user.User;
import com.hk.domain.inventory.rv.ReconciliationStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA. * User: Neha * Date: Feb 6, 2013 * Time: 1:07:11 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings ("serial")
@Entity
@Table(name = "reverse_order")
@NamedQuery(name = "getReverseOrderById", query = "select rvo from ReverseOrder rvo where id = :reverseOrderId")
@Deprecated
public class ReverseOrder implements java.io.Serializable {

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

	@Column (name = "amount", precision = 11)
	private Double amount;

	@Column (name = "return_reason")
	private String returnReason;

	@Column (name = "reverse_order_type")
	private String reverseOrderType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column (name = "action_proposed")
	private String actionProposed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn (name = "reconciliation_status_id")
	private ReconciliationStatus reconciliationStatus;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate              = new Date();

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "receive_dt", length = 19)
    private Date receivedDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reverseOrder")
	private Set<ReverseLineItem> reverseLineItems               = new HashSet<ReverseLineItem>();

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

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set<ReverseLineItem> getReverseLineItems() {
		return reverseLineItems;
	}

	public void setReverseLineItems(Set<ReverseLineItem> reverseLineItems) {
		this.reverseLineItems = reverseLineItems;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getReverseOrderType() {
		return reverseOrderType;
	}

	public void setReverseOrderType(String reverseOrderType) {
		this.reverseOrderType = reverseOrderType;
	}

	@Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

}
