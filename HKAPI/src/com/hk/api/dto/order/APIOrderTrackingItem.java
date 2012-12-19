package com.hk.api.dto.order;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 17, 2012
 * Time: 3:34:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class APIOrderTrackingItem {
    private String status;
    private Long qty;
    private String itemName;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
