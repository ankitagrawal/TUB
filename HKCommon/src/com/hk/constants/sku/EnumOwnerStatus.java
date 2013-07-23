package com.hk.constants.sku;

import com.hk.domain.sku.SkuItemOwner;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 23, 2013
 * Time: 4:24:49 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumOwnerStatus {

    SELF(10L, "SELF"),
    CUSTOMER(20L, "CUSTOMER"),
    SUPPLIER(30L, "SUPPLIER"),
    BRIGHT(40L, "BRIGHT");


    EnumOwnerStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public SkuItemOwner getSkuItemOwnerStatus() {
		SkuItemOwner skuItemOwner = new SkuItemOwner();
		skuItemOwner.setId(this.id);
		skuItemOwner.setName(this.name);
		return skuItemOwner;
	}


    private Long id;
    private String name;


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
