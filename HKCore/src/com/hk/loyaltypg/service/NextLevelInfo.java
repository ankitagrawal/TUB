package com.hk.loyaltypg.service;

import com.hk.domain.loyaltypg.Badge;

public class NextLevelInfo {

	private Badge existingBadge;
	private Badge nextBadge;
	private double currentSpend;
	private double spendRequired;

	public Badge getExistingBadge() {
		return this.existingBadge;
	}

	public void setExistingBadge(Badge existingBadge) {
		this.existingBadge = existingBadge;
	}

	public Badge getNextBadge() {
		return this.nextBadge;
	}

	public void setNextBadge(Badge nextBadge) {
		this.nextBadge = nextBadge;
	}

	public double getCurrentSpend() {
		return this.currentSpend;
	}

	public void setCurrentSpend(double currentSpend) {
		this.currentSpend = currentSpend;
	}

	public double getSpendRequired() {
		return this.spendRequired;
	}

	public void setSpendRequired(double spendRequired) {
		this.spendRequired = spendRequired;
	}

}
