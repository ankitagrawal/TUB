package com.hk.domain.api;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 8/14/13
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class HKAPIBookingInfo {

    private Double mrp;
    private Long qty;
    private Long cartLineItemId;
    private Long lineItemId;
    private Long warehouseId;
    private String productVariantId;

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Long getCartLineItemId() {
        return cartLineItemId;
    }

    public void setCartLineItemId(Long cartLineItemId) {
        this.cartLineItemId = cartLineItemId;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }
}
