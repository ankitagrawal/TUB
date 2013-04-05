package com.hk.loyaltypg.service;

import java.util.Collection;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.user.User;

public interface LoyaltyProgramService {
	
	public List<LoyaltyProduct> listProucts(int startRow, int maxRows);
	
	public int countProucts();
	
	public LoyaltyProduct getProductByVariantId(String variantId);
	
	public void creditKarmaPoints(Long orderId);
	
	public void debitKarmaPoints(Long orderId);

	public void approveKarmaPoints(Long orderId);
	
	public void cancelKarmaPoints(Long orderId);

	public double aggregatePoints(Collection<CartLineItem> cartLineItems);
	
	public double calculateKarmaPoints(Long userId);
	
	public double aggregatePoints(Long orderId);
	
	public UserBadgeInfo getUserBadgeInfo(Long userId);

	public List<Badge> getAllBadges();
	
	/**
	 * This method returns an user's karma profile history.
	 * @param user
	 * @param page
	 * @param perPage
	 * @return
	 */
	public Page getProfileHistory (User user, int page, int perPage);
}
