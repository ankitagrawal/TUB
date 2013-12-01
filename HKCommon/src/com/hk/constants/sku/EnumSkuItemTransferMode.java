package com.hk.constants.sku;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jan 30, 2013
 * Time: 5:00:12 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSkuItemTransferMode {

    STOCK_TRANSFER_OUT(10L, "Stock Transfer Out"),
    STOCK_TRANSFER_IN(20L, "Stock Transfer In"),
    RV_LINEITEM_OUT(30L, "RVLineItem"),
    CYCLE_COUNT(40L, "CycleCount"),
    CYCLE_COUNT_SKU_ITEM_MISSED(50L, "CCSkuItemMissed");

    private String name;
    private Long id;

    EnumSkuItemTransferMode(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

}
