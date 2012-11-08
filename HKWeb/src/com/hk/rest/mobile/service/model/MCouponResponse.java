package com.hk.rest.mobile.service.model;


public class MCouponResponse {

	private String id;
	private String offerDescription;
	private String offerTerms;
	private String endDate;
	private String couponCode;
	private boolean selectedOffer;
	
	public boolean isSelectedOffer() {
		return selectedOffer;
	}
	public void setSelectedOffer(boolean selectedOffer) {
		this.selectedOffer = selectedOffer;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOfferDescription() {
		return offerDescription;
	}
	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}
	public String getOfferTerms() {
		return offerTerms;
	}
	public void setOfferTerms(String offerTerms) {
		this.offerTerms = offerTerms;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}	
}