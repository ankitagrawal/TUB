package com.hk.rest.mobile.service.model;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 20, 2012
 * Time: 6:26:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MRequest {

    private String operation = null;
    private Object request;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getResult() {
        return request;
    }

    public void setResult(Object request) {
        this.request = request;
    }
}