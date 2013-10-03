package com.hk.edge.request;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class VariantStockSyncRequest {

    private String  oldVariantId;
    private Double  mrp;
    private Double  costPrice;
    private boolean oos;

    public String getOldVariantId() {
        return oldVariantId;
    }

    public void setOldVariantId(String oldVariantId) {
        this.oldVariantId = oldVariantId;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public boolean isOos() {
        return oos;
    }

    public void setOos(boolean oos) {
        this.oos = oos;
    }

    public Map<String, String> getParameters() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldVariantId", this.oldVariantId);
        params.put("mrp", this.mrp.toString());
        params.put("costPrice", this.costPrice.toString());
        params.put("oos", Boolean.valueOf(this.oos).toString());

        return params;
    }

}
