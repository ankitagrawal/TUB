package com.hk.loyaltypg.service;

import com.hk.domain.loyaltypg.Badge;

public class UserBadgeInfo {

	private Long userId;
	private double loyaltyPoints;
	private Badge badge;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public double getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(double loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public Badge getBadge() {
		return badge;
	}

	public void setBadge(Badge badge) {
		this.badge = badge;
	}

}
