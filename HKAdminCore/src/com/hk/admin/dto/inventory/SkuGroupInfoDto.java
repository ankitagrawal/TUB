package com.hk.admin.dto.inventory;

import com.hk.domain.sku.SkuGroup;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jul 25, 2013
 * Time: 5:51:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SkuGroupInfoDto {

    SkuGroup skuGroup;
    Long qty;

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
}
