package com.hk.admin.dto;

import java.util.Date;

public class NdrDto{

    String hubName;
    String awbNumber;
    Date createDate;
    Integer aging;
    String nonDeliveryReason;
    Integer numberOfAttempts;
    String owner;
    String status;
    String ndrResolution;
    Date futureDate;
    String remarks;
    Long consignmentId;
    Long consignmentTrackingId;
    Long runsheetId;

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getAging() {
        return aging;
    }

    public void setAging(Integer aging) {
        this.aging = aging;
    }

    public String getNonDeliveryReason() {
        return nonDeliveryReason;
    }

    public void setNonDeliveryReason(String nonDeliveryReason) {
        this.nonDeliveryReason = nonDeliveryReason;
    }

    public Integer getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(Integer numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNdrResolution() {
        return ndrResolution;
    }

    public void setNdrResolution(String ndrResolution) {
        this.ndrResolution = ndrResolution;
    }

    public Date getFutureDate() {
        return futureDate;
    }

    public void setFutureDate(Date futureDate) {
        this.futureDate = futureDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    public Long getConsignmentTrackingId() {
        return consignmentTrackingId;
    }

    public void setConsignmentTrackingId(Long consignmentTrackingId) {
        this.consignmentTrackingId = consignmentTrackingId;
    }

    public Long getRunsheetId() {
        return runsheetId;
    }

    public void setRunsheetId(Long runsheetId) {
        this.runsheetId = runsheetId;
    }

}