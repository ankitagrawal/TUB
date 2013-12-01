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
}
