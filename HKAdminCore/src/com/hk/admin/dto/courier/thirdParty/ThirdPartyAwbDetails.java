package com.hk.admin.dto.courier.thirdParty;

import java.util.List;

public class ThirdPartyAwbDetails {

    private List<String> barcodeList;

    private List<String> routingCode;

    private String       trackingNumber;

    private boolean      cod;

    private String       pincode;

//    private boolean      isGroundShipping;

    public ThirdPartyAwbDetails(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<String> getBarcodeList() {
        return barcodeList;
    }

    public void setBarcodeList(List<String> barcodeList) {
        this.barcodeList = barcodeList;
    }

    public List<String> getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(List<String> routingCode) {
        this.routingCode = routingCode;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

//    public boolean isGroundShipping() {
//        return isGroundShipping;
//    }
//
//    public void setGroundShipping(boolean groundShipping) {
//        isGroundShipping = groundShipping;
//    }
}
