package com.hk.domain.sku;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 3:02:16 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "sku_item_cart_line_item")
public class SkuItemCLI implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;

	@JsonSkip
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_item_id", nullable = false)
	private SkuItem skuItem;

	@JsonSkip
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_line_item_id", nullable = false)
	private CartLineItem cartLineItem;

	@Column(name = "unit_num", nullable = false)
	private Long unitNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public SkuItem getSkuItem() {
		return skuItem;
	}

	public void setSkuItem(SkuItem skuItem) {
		this.skuItem = skuItem;
	}

	public CartLineItem getCartLineItem() {
		return cartLineItem;
	}

	public void setCartLineItem(CartLineItem cartLineItem) {
		this.cartLineItem = cartLineItem;
	}

	public Long getUnitNum() {
		return unitNum;
	}

	public void setUnitNum(Long unitNumber) {
		this.unitNum = unitNumber;
	}
}
