package com.hk.impl.service;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;

public class GenericResponseFromEdge {

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

}
