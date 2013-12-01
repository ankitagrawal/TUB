package com.hk.pojo.payment;

/*
 * User: Pratham
 * Date: 05/03/13  Time: 13:31
*/
public class PaymentResponseDto {


    private String gatewayOrderId;
    private String responseCode;
    private Double amount;
    private String responseMessage;
    private String transactionType;
    private String hkpayReferenceId;
    private String gatewayName;
    private String issuerName;
    private Long gatewayId;
    private Long issuerId;



    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getHkpayReferenceId() {
        return hkpayReferenceId;
    }

    public void setHkpayReferenceId(String hkpayReferenceId) {
        this.hkpayReferenceId = hkpayReferenceId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }



}
