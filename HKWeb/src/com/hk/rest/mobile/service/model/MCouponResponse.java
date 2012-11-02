package com.hk.rest.mobile.service.model;

import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;

public class MCouponResponse {

	private String id;
	private String offerDescription;
	private String offerTerms;
	private String endDate;
	private String couponCode;
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