package com.hk.dto.email;

public class BulkEmailDto {
    String unsubscribeLink;
    String emailId;
    String couponCode;

    public String getUnsubscribeLink() {
        return unsubscribeLink;
    }

    public void setUnsubscribeLink(String unsubscribeLink) {
        this.unsubscribeLink = unsubscribeLink;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
