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
	
	public List<LoyaltyProduct> listProucts(SearchCriteria criteria);
	
	public int countProucts(SearchCriteria criteria);
	
	public LoyaltyProduct getProductByVariantId(String variantId);
	
	public void creditKarmaPoints(Order order);
	
	public void debitKarmaPoints(Order order);

	public void approveKarmaPoints(Order order);
	
	public void cancelLoyaltyPoints(Order order);

	public UserBadgeInfo getUserBadgeInfo(User user);

	public List<Badge> getAllBadges();
	
	public Page getUserLoyaltyProfileHistory (User user, int page, int perPage);
	
	public List<CategoryDto> listCategories();

	public double calculateLoyaltyPoints(User user);
	
	public double calculateLoyaltyPoints(Order order);
	
	public double calculateLoyaltyPoints(Collection<CartLineItem> cartLineItems);

	double calculateAnualSpend(User user);
}
