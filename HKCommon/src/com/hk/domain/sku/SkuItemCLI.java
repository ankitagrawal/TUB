package com.hk.domain.sku;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 3:02:16 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "sku_item_cart_line_item" , uniqueConstraints = @UniqueConstraint(columnNames = {"cart_line_item_id", "unit_num"}))
public class SkuItemCLI implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "skuItemCLI")
	private List<SkuItemLineItem> skuItemLineItems;

	@JsonSkip
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 19)
	private Date createDate = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", length = 19)
	private Date updateDate;

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

	public List<SkuItemLineItem> getSkuItemLineItems() {
		return skuItemLineItems;
	}

	public void setSkuItemLineItems(List<SkuItemLineItem> skuItemLineItems) {
		this.skuItemLineItems = skuItemLineItems;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof SkuItemCLI) {
			SkuItemCLI skuLineItem = (SkuItemCLI) obj;
			if (this.id.equals(skuLineItem.getId()))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
