package com.hk.admin.dto.courier.thirdParty;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jan 17, 2013
 * Time: 4:09:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThirdPartyTrackDetails {

	private String trackingNo;
	private String referenceNo;
	private String awbStatus;
	private String deliveryDate;

	public ThirdPartyTrackDetails(){
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getAwbStatus() {
		return awbStatus;
	}

	public void setAwbStatus(String awbStatus) {
		this.awbStatus = awbStatus;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}
