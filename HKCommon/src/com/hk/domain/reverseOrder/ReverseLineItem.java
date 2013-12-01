package com.hk.domain.reverseOrder;

import com.hk.domain.shippingOrder.LineItem;
import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.  * User: Neha * Date: Feb 6, 2013 * Time: 12:55:13 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings ("serial")
@Entity
@Table(name = "reverse_line_item")
public class ReverseLineItem {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_order_id")
	private ReverseOrder reverseOrder;

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_line_item_id")
	private LineItem referredLineItem;

	@Column(name = "return_qty")
	private Long returnQty;

	@JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate         = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReverseOrder getReverseOrder() {
		return reverseOrder;
	}

	public void setReverseOrder(ReverseOrder reverseOrder) {
		this.reverseOrder = reverseOrder;
	}

	public LineItem getReferredLineItem() {
		return referredLineItem;
	}

	public void setReferredLineItem(LineItem referredLineItem) {
		this.referredLineItem = referredLineItem;
	}

	public Long getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Long returnQty) {
		this.returnQty = returnQty;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
