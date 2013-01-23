package com.hk.admin.dto.pos;

import com.hk.domain.sku.SkuGroup;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/22/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class POSLineItemDto {

	private String productName;
	private SkuGroup skuGroup;
	private Long qty;
	private Double mrp;
	private Double offerPrice;
	private Double total;

	public SkuGroup getSkuGroup() {
		return skuGroup;
	}

	public void setSkuGroup(SkuGroup skuGroup) {
		this.skuGroup = skuGroup;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(Double offerPrice) {
		this.offerPrice = offerPrice;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
