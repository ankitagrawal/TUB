package com.hk.admin.dto;

import com.hk.domain.user.User;


public class ConsignmentDto {

    private String awbNumber;
    private String cnnNumber;
    private String paymentMode;
    private String address;
    private Double amount;
    private User   transferredToAgent;

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public String getCnnNumber() {
        return cnnNumber;
    }

    public void setCnnNumber(String cnnNumber) {
        this.cnnNumber = cnnNumber;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getTransferredToAgent() {
        return transferredToAgent;
    }

    public void setTransferredToAgent(User transferredToAgent) {
        this.transferredToAgent = transferredToAgent;
    }
}
