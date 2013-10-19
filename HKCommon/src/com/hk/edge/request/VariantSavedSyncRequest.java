package com.hk.edge.request;

public class VariantSavedSyncRequest extends VariantPricingSyncRequest {

    private String  oldProductId;
    private double  mrp;            // synced only in case of jit
    private long    minDispatchDays;
    private long    maxDispatchDays;

    private boolean codAllowed;
    private boolean jit;
    private boolean deleted;

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public long getMinDispatchDays() {
        return minDispatchDays;
    }

    public void setMinDispatchDays(long minDispatchDays) {
        this.minDispatchDays = minDispatchDays;
    }

    public long getMaxDispatchDays() {
        return maxDispatchDays;
    }

    public void setMaxDispatchDays(long maxDispatchDays) {
        this.maxDispatchDays = maxDispatchDays;
    }

    public boolean isCodAllowed() {
        return codAllowed;
    }

    public void setCodAllowed(boolean codAllowed) {
        this.codAllowed = codAllowed;
    }

    public boolean isJit() {
        return jit;
    }

    public void setJit(boolean jit) {
        this.jit = jit;
    }

    public String getOldProductId() {
        return oldProductId;
    }

    public void setOldProductId(String oldProductId) {
        this.oldProductId = oldProductId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    @Override
    public String toString() {
      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append(oldVariantId).append(oldVariantId).append(" op: ").append(offerPrice).append(" dis :").append(discount);
      strBuilder.append(" mrp :").append(mrp).append(" oldPRoductId: " ).append(oldProductId).append(" minD: ").append(minDispatchDays);
      strBuilder.append(" maxD:").append(maxDispatchDays);

      return strBuilder.toString();

    }

}
