package com.hk.domain.sku;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 8/14/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "foreign_si_cli")
public class ForeignSkuItemCLI {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name="foreign_sku_item_id")
  private Long skuItemId;
  
  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="cart_line_item_id")
  private CartLineItem cartLineItem;

  @Column(name="foreign_shipping_order_id")
  private Long foreignShippingOrderId;

  @Column(name="foreign_base_order_id")
  private Long foreignOrderId;

  @Column(name = "unit_num", nullable = false)
  private Long unitNum;
  
  @Column(name="counter")
  private Long counter;

  @JsonSkip
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_dt", nullable = false, length = 19)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_dt", length = 19)
  private Date updateDate;

  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id", nullable = false)
  private ProductVariant productVariant;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUnitNum() {
    return unitNum;
  }

  public void setUnitNum(Long unitNum) {
    this.unitNum = unitNum;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getForeignShippingOrderId() {
    return foreignShippingOrderId;
  }

  public void setForeignShippingOrderId(Long foreignShippingOrderId) {
    this.foreignShippingOrderId = foreignShippingOrderId;
  }

  public Long getForeignOrderId() {
    return foreignOrderId;
  }

  public void setForeignOrderId(Long foreignOrderId) {
    this.foreignOrderId = foreignOrderId;
  }

	public Long getSkuItemId() {
		return skuItemId;
	}

	public void setSkuItemId(Long skuItemId) {
		this.skuItemId = skuItemId;
	}

	public Long getCounter() {
		return counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public CartLineItem getCartLineItem() {
		return cartLineItem;
	}

	public void setCartLineItem(CartLineItem cartLineItem) {
		this.cartLineItem = cartLineItem;
	}
}
