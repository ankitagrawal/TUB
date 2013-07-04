package com.hk.api.dto.accounts;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 4, 2012
 * Time: 1:42:47 AM
 */
public class HKAPIPaymentInfoDTO {
    private String tinNumber;
    private String date;
    private String busyPaymentId;
    private Double amount;
    private Double busySupplierBalance;
    private String narration;

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBusyPaymentId() {
        return busyPaymentId;
    }

    public void setBusyPaymentId(String busyPaymentId) {
        this.busyPaymentId = busyPaymentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBusySupplierBalance() {
        return busySupplierBalance;
    }

    public void setBusySupplierBalance(Double busySupplierBalance) {
        this.busySupplierBalance = busySupplierBalance;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}
