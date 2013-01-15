package com.hk.api.loyaltypg.service;

import java.util.List;

import com.hk.api.loyaltypg.domain.LoyaltyProduct;

public interface LoyaltyProgramService {
	
	List<LoyaltyProduct> listProucts(Long userId, int startRow, int maxRows);

	void reconcileHistoryPurchase(Long userId);
	
	int calculateKarmaPoints(Long userId);

	void creditKarmaPoints(Long orderId);
	
	void debitKarmaPoints(Long orderId);

	void approveKarmaPoints(Long orderId);
	
}
