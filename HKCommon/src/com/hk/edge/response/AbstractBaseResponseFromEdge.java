package com.hk.edge.response;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.domain.core.JSONObject;
import com.hk.edge.constants.DtoJsonConstants;

@SuppressWarnings("serial")
public class AbstractBaseResponseFromEdge extends JSONObject {

    @JsonProperty(DtoJsonConstants.EXCEPTION)
    protected boolean      exception = false;

    @JsonProperty(DtoJsonConstants.MESSAGES)
    protected List<String> messages  = new ArrayList<String>();

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    protected String[] getKeys() {
        return null;
    }

    @Override
    protected Object[] getValues() {
        return null;
    }

}
