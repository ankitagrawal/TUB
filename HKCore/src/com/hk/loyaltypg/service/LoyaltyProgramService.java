package com.hk.loyaltypg.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.FileBean;

import com.akube.framework.dao.Page;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.loyaltypg.UserBadgeInfo;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
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

	public Collection<Badge> getAllBadges();
	
	public Page getUserLoyaltyProfileHistory (User user, int page, int perPage);
	
	public List<CategoryDto> listCategories();

	public double calculateLoyaltyPoints(User user);
	
	public double calculateLoyaltyPoints(Order order);
	
	public double calculateLoyaltyPoints(Collection<CartLineItem> cartLineItems);

	public double calculateAnualSpend(User user);
	
	public NextLevelInfo fetchNextLevelInfo(User user);
	
	public double convertLoyaltyToRewardPoints(User user);

	public void updateUserBadgeInfo(User user);

	public void uploadLoyaltyProductsCSV(FileBean csvFileReader, List<String> errorMessages);

	public void uploadBadgeInfoCSV(FileBean csvFileReader, List<String> errorMessages);

	public void createNewUserBadgeInfo(User user);
	
	public UserOrderKarmaProfile getUserOrderKarmaProfile(Long orderId);

	public String updateCardNumber(UserBadgeInfo info, String cardNumber);

	public List<UserOrderKarmaProfile> getUserLoyaltyProfileHistory(User user);

	public List<LoyaltyProduct> searchLoyaltyProducts(Map<String, String> keywordsMap);

	public List<UserOrderKarmaProfile> searchKarmaProfiles(Map<String, Object> searchMap);

}
