package com.hk.edge.response.variant;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class StoreVariantBasicResponse extends AbstractStoreVariantResponse {

    @JsonProperty(DtoJsonConstants.COD_ALLOWED)
    private boolean codAllowed;
    @JsonProperty(DtoJsonConstants.OOS)
    private boolean oos;

    @JsonProperty(DtoJsonConstants.MIN_DISPATCH_DAYS)
    private Long    minDispatchDays;
    @JsonProperty(DtoJsonConstants.MAX_DISPATCH_DAYS)
    private Long    maxDispatchDays;

    public boolean isCodAllowed() {
        return codAllowed;
    }

    public void setCodAllowed(boolean codAllowed) {
        this.codAllowed = codAllowed;
    }

    public boolean isOos() {
        return oos;
    }

    public void setOos(boolean oos) {
        this.oos = oos;
    }

    public Long getMinDispatchDays() {
        return minDispatchDays;
    }

    public void setMinDispatchDays(Long minDispatchDays) {
        this.minDispatchDays = minDispatchDays;
    }

    public Long getMaxDispatchDays() {
        return maxDispatchDays;
    }

    public void setMaxDispatchDays(Long maxDispatchDays) {
        this.maxDispatchDays = maxDispatchDays;
    }

    @Override
    protected String[] getKeys() {
        return null;
    }

    @Override
    protected Object[] getValues() {
        return null;
    }

    /*
     * @Override protected List<String> getKeys() { List<String> keyList = super.getKeys();
     * keyList.add(DtoJsonConstants.OOS); keyList.add(DtoJsonConstants.COD_ALLOWED);
     * keyList.add(DtoJsonConstants.MIN_DISPATCH_DAYS); keyList.add(DtoJsonConstants.MAX_DISPATCH_DAYS); return keyList; }
     * @Override protected List<Object> getValues() { List<Object> valueList = super.getValues();
     * valueList.add(this.oos); valueList.add(this.codAllowed); valueList.add(this.minDispatchDays);
     * valueList.add(this.maxDispatchDays); return valueList; }
     */
}
