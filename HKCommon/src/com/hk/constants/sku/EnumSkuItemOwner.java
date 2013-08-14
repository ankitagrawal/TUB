package com.hk.constants.sku;

import com.hk.domain.sku.SkuItemOwner;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 4:24:49 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSkuItemOwner {

	SELF(10L, "SELF"),
	CUSTOMER(20L, "CUSTOMER"),
	SUPPLIER(30L, "SUPPLIER"),
	BRIGHT(40L, "BRIGHT");
	private Long id;
	private String name;


	EnumSkuItemOwner(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public SkuItemOwner getSkuItemOwnerStatus() {
		SkuItemOwner skuItemOwner = new SkuItemOwner();
		skuItemOwner.setId(this.id);
		skuItemOwner.setName(this.name);
		return skuItemOwner;
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
