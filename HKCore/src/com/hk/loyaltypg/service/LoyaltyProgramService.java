package com.hk.loyaltypg.service;

import java.util.Collection;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserBadgeInfo;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.user.User;
import com.hk.loyaltypg.dto.CategoryLoyaltyDto;
import com.hk.store.SearchCriteria;

public interface LoyaltyProgramService {
	
	public List<LoyaltyProduct> listProucts(SearchCriteria criteria);
	
	public int countProucts(SearchCriteria criteria);
	
	public LoyaltyProduct getProductByVariantId(String variantId);
	
	public void creditKarmaPoints(Long orderId);
	
	public void debitKarmaPoints(Long orderId);

	public void approveKarmaPoints(Long orderId);
	
	public void cancelLoyaltyPoints(Long orderId);

	public UserBadgeInfo getUserBadgeInfo(Long userId);

	public List<Badge> getAllBadges();
	
	public Page getUserLoyaltyProfileHistory (User user, int page, int perPage);
	
	public List<CategoryLoyaltyDto> getLoyaltyCatalog();

	public List<LoyaltyProduct> getProductsByPoints(double minPoints, double maxPoints);
	
	public double calculateValidPoints(Long userId);
	
	public double calculateAnnualSpend(User user);
	
	public void reviseBadgeInfoForUser(User user, Double amount);
	
	public void reviseUserBadgeInfo(User user);
	
	public double calculateUpgradePoints(UserBadgeInfo info);
	
	public double calculateLoyaltyPointsForOrder(Long orderId);
	
	public double calculateLoyaltyPointsForCart(Collection<CartLineItem> cartLineItems);
}
