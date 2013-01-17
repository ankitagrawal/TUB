package com.hk.loyaltypg.service;

import java.util.List;

import com.hk.domain.loyaltypg.LoyaltyProduct;

public interface LoyaltyProgramService {
	
	List<LoyaltyProduct> listProucts(Long userId, int startRow, int maxRows);
	
	LoyaltyProduct getProductByVariantId(String variantId);
	
	void reconcileHistoryPurchase(Long userId);
	
	int calculateKarmaPoints(Long userId);

	void creditKarmaPoints(Long orderId);
	
	void debitKarmaPoints(Long orderId);

	void approveKarmaPoints(Long orderId);
	
}
