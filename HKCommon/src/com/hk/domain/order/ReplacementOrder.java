package com.hk.domain.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

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
}
