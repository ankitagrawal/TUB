package com.hk.pojo;


import com.hk.domain.core.PaymentStatus;
import com.hk.domain.payment.Gateway;


/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 6/3/13
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class HkPaymentResponse {

    private String gatewayOrderId;
    private String gatewayReferenceId;
    private String responseMsg;
    private Gateway gateway;
    private PaymentStatus paymentStatus;
    private String errorLog;
    private String rrn;
    private Double amount;
    private String transactionType;
    private String authIdCode;

    public HkPaymentResponse(String gatewayOrderId, String gatewayReferenceId, String responseMsg, Gateway gateway, PaymentStatus paymentStatus, String errorLog, String rrn,String authIdOCode ,Double amount) {
        this.gatewayOrderId = gatewayOrderId;
        this.gatewayReferenceId = gatewayReferenceId;
        this.responseMsg = responseMsg;
        this.gateway = gateway;
        this.paymentStatus = paymentStatus;
        this.errorLog = errorLog;
        this.rrn = rrn;
        this.authIdCode = authIdOCode;
        this.amount = amount;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public String getGatewayReferenceId() {
        return gatewayReferenceId;
    }

    public void setGatewayReferenceId(String gatewayReferenceId) {
        this.gatewayReferenceId = gatewayReferenceId;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAuthIdCode() {
        return authIdCode;
    }

    public void setAuthIdCode(String authIdCode) {
        this.authIdCode = authIdCode;
    }
}
