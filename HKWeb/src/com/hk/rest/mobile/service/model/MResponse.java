package com.hk.rest.mobile.service.model;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 20, 2012
 * Time: 6:27:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MResponse {
    int status = 0;
    MErrors errors = null;
    String operation = null;
    Object result;
    Boolean hasMore = new Boolean(false);

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MErrors getErrors() {
        return errors;
    }

    public void setErrors(MErrors errors) {
        this.errors = errors;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}