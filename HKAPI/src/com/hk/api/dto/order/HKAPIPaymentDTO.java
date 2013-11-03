package com.hk.api.dto.order;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 */
public class HKAPIPaymentDTO {
    private String paymentmodeId;
    //bankId is not used right now
    private String bankId;
    private Long gatewayId;
    private String status;
    private String gatewayOrderId;
    private String responseMessage;
    private String paymentChecksum;
    private String authIdCode;
    private Long issuerId;

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }


    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }


    public String getPaymentChecksum() {
        return paymentChecksum;
    }

    public void setPaymentChecksum(String paymentChecksum) {
        this.paymentChecksum = paymentChecksum;
    }


    public String getAuthIdCode() {
        return authIdCode;
    }

    public void setAuthIdCode(String authIdCode) {
        this.authIdCode = authIdCode;
    }



    public String getPaymentmodeId() {
        return paymentmodeId;
    }

    public void setPaymentmodeId(String paymentmodeId) {
        this.paymentmodeId = paymentmodeId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }
    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }
}
