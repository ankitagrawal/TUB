package com.hk.loyaltypg.service;

import java.util.Collection;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserBadgeInfo;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.store.CategoryDto;
import com.hk.store.SearchCriteria;

public interface LoyaltyProgramService {
	
	List<LoyaltyProduct> listProucts(SearchCriteria criteria);
	
	int countProucts(SearchCriteria criteria);
	
	LoyaltyProduct getProductByVariantId(String variantId);
	
	void creditKarmaPoints(Order order);
	
	void debitKarmaPoints(Order order);

	void approveKarmaPoints(Order order);
	
	void cancelLoyaltyPoints(Order order);

	UserBadgeInfo getUserBadgeInfo(User user);

	Collection<Badge> getAllBadges();
	
	Page getUserLoyaltyProfileHistory (User user, int page, int perPage);
	
	List<CategoryDto> listCategories();

	double calculateLoyaltyPoints(User user);
	
	double calculateLoyaltyPoints(Order order);
	
	double calculateLoyaltyPoints(Collection<CartLineItem> cartLineItems);

	double calculateAnualSpend(User user);
	
	NextLevelInfo fetchNextLevelInfo(User user);
	
	public double convertLoyaltyToRewardPoints(User user);
}
