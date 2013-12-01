package com.hk.constants.coupon;

import com.hk.domain.coupon.CouponType;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 8/21/12
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumCouponType {
	REFERRAL(10L, "Referral"),
	AFFILIATE(20L, "Affiliate"),
	OFFER(30L, "Offer"),
	B2B(40L, "B2B"),;

	private java.lang.String name;
	private java.lang.Long id;

	EnumCouponType(Long id, java.lang.String name) {
		this.name = name;
		this.id = id;
	}

	public CouponType asCouponType() {
		CouponType couponType = new CouponType();
		couponType.setId(id);
		couponType.setName(name);
		return couponType;
	}


	public java.lang.String getName() {
		return name;
	}

	public java.lang.Long getId() {
		return id;
	}
}
