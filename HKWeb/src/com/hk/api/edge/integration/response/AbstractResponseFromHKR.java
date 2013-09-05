package com.hk.api.edge.integration.response;

import java.util.ArrayList;
import java.util.List;

import com.hk.api.edge.constants.StoreConstants;
import com.hk.domain.core.JSONObject;

/**
 * this has to be in sync with the base response class for edge, since this resposne will be given to web for
 * integration, example: cart earlier being fetched from edge api will for now be served from hkr, so keep this in sync
 * with edge base response class
 * 
 * @author vaibhav.adlakha
 */
public abstract class AbstractResponseFromHKR extends JSONObject {

    protected boolean      exception = false;
    protected List<String> msgs      = new ArrayList<String>();

    protected Long         storeId   = StoreConstants.DEFAULT_STORE_ID;

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public List<String> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

}
