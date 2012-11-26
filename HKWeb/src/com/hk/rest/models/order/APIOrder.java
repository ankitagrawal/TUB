package com.hk.rest.models.order;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.rest.models.user.APIUser;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 2, 2012
 * Time: 4:54:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIOrder {
    @JsonProperty("user")
    private APIUser apiUser;
    @JsonProperty("order")
    private APIOrderDetails apiOrderDetails;
    @JsonProperty("payment")
    private APIPayment apiPayment;
    @JsonProperty("address")
    private APIAddress apiAddress;

    public APIUser getApiUser() {
        return apiUser;
    }


    public void setApiUser(APIUser apiUser) {
        this.apiUser = apiUser;
    }

    public APIOrderDetails getApiOrderDetails() {
        return apiOrderDetails;
    }


    public void setApiOrderDetails(APIOrderDetails apiOrderDetails) {
        this.apiOrderDetails = apiOrderDetails;
    }


    public APIPayment getApiPayment() {
        return apiPayment;
    }


    public void setApiPayment(APIPayment apiPayment) {
        this.apiPayment = apiPayment;
    }


    public APIAddress getApiAddress() {
        return apiAddress;
    }


    public void setApiAddress(APIAddress apiAddress) {
        this.apiAddress = apiAddress;
    }

}
