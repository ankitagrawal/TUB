package com.hk.edge;

import java.util.ArrayList;
import java.util.List;

import com.hk.domain.core.JSONObject;

public abstract class AbstractApiBaseResponse extends JSONObject {

    protected boolean      exception = false;
    protected List<String> msgs      = new ArrayList<String>();

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

}
