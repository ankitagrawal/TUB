package com.hk.domain.order;
 
import com.hk.domain.reverseOrder.ReverseOrder;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 4, 2012
 * Time: 2:55:12 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "replacement_order")
@PrimaryKeyJoinColumn(name="so_ro_id")
public class ReplacementOrder extends ShippingOrder implements Serializable{
  @Column(name = "is_rto")
  private boolean rto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ref_shipping_order_id")
  private ShippingOrder refShippingOrder;

	@OneToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "reverse_order_id")
  	private ReverseOrder reverseOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "replacement_order_reason_id")
	private ReplacementOrderReason replacementOrderReason;

	public boolean isRto() {
    return rto;
  }

  public void setRto(Boolean rto) {
    this.rto = rto;
  }

  public ShippingOrder getRefShippingOrder() {
    return refShippingOrder;
  }

  public void setRefShippingOrder(ShippingOrder refShippingOrder) {
    this.refShippingOrder = refShippingOrder;
  }

	public ReplacementOrderReason getReplacementOrderReason() {
		return replacementOrderReason;
	}

	public void setReplacementOrderReason(ReplacementOrderReason replacementOrderReason) {
		this.replacementOrderReason = replacementOrderReason;
	}

	public ReverseOrder getReverseOrder() {
		return reverseOrder;
	}

	public void setReverseOrder(ReverseOrder reverseOrder) {
		this.reverseOrder = reverseOrder;
	}
}
