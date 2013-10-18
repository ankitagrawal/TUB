package com.hk.domain.api;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit
 * Date: 8/15/13
 * Time: 4:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class HKApiSkuResponse {
    String variantId;
    String tinPrefix;
    Long qty;
    Double mrp;

    Long warehouseId;
    Double costPrice;
    Double hkPrice;
    boolean outOfStock;
    Long netQty;
    String fcCode;

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getTinPrefix() {
        return tinPrefix;
    }

    public void setTinPrefix(String tinPrefix) {
        this.tinPrefix = tinPrefix;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }


    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getNetQty() {
        return netQty;
    }

    public void setNetQty(Long netQty) {
        this.netQty = netQty;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public Double getHkPrice() {
        return hkPrice;
    }

    public void setHkPrice(Double hkPrice) {
        this.hkPrice = hkPrice;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public String getFcCode() {
     return fcCode;
    }

    public void setFcCode(String fcCode) {
     this.fcCode = fcCode;
   }
}