package com.hk.dto.payment;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 12/5/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentEnquiry {
    private Long enqCode;
    private String enqMessage;
    @SerializedName("enqPaymentList")
    private List<EnqPaymentList> list = new ArrayList<EnqPaymentList>();

    public List<EnqPaymentList> getList() {
        return list;
    }

    public void setList(List<EnqPaymentList> list) {
        this.list = list;
    }

    public String getEnqMessage() {
        return enqMessage;
    }

    public void setEnqMessage(String enqMessage) {
        this.enqMessage = enqMessage;
    }

    public Long getEnqCode() {
        return enqCode;
    }

    public void setEnqCode(Long enqCode) {
        this.enqCode = enqCode;
    }
}
