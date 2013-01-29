package com.hk.loyaltypg.service;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.loyaltypg.LoyaltyProduct;

public interface LoyaltyProgramService {
	
	List<LoyaltyProduct> listProucts(Long userId, int startRow, int maxRows);
	
	LoyaltyProduct getProductByVariantId(String variantId);
	
	void reconcileHistoryPurchase(Long userId);
	
	double calculateKarmaPoints(Long userId);
	
	double calculateDebitPoints(Long orderId);

	void creditKarmaPoints(Long orderId);
	
	void debitKarmaPoints(Long orderId);

	void approveKarmaPoints(Long orderId);

}
