package com.hk.constants.sku;

import com.hk.domain.sku.SkuItemStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/26/12
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSkuItemStatus {
	Checked_IN(10L, "CHECKED IN"),
	Checked_OUT(20L, "CHECKED OUT"),
	Stock_Transfer_Out(30L, "Stock Transfer Out"),
	Damaged (40L, "Damaged"),
	Expired (50L, "Expired");

	private Long id;
	private String name;

	EnumSkuItemStatus(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public SkuItemStatus getSkuItemStatus() {
		SkuItemStatus skuItemStatus = new SkuItemStatus();
		skuItemStatus.setId(this.id);
		skuItemStatus.setName(this.name);
		return skuItemStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
