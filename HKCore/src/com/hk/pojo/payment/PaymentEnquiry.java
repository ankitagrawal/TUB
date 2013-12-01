package com.hk.pojo.payment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 7/18/13
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentEnquiry {

    private Long enqCode;
    private String enqMessage;
    private List<PaymentResponseDto> enqPaymentList;

    public Long getEnqCode() {
        return enqCode;
    }

    public void setEnqCode(Long enqCode) {
        this.enqCode = enqCode;
    }

    public String getEnqMessage() {
        return enqMessage;
    }

    public void setEnqMessage(String enqMessage) {
        this.enqMessage = enqMessage;
    }

    public List<PaymentResponseDto> getEnqPaymentList() {
        return enqPaymentList;
    }

    public void setEnqPaymentList(List<PaymentResponseDto> enqPaymentList) {
        this.enqPaymentList = enqPaymentList;
    }
}
