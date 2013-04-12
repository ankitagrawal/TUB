package com.hk.loyaltypg.dao;

import java.util.List;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.pact.dao.BaseDao;

public interface LoyaltyProductDao extends BaseDao {

	LoyaltyProduct getProductbyVarientId(String variantId);
	
	/**
	 * This method lists all the categories for Loyalty products
	 * @return
	 */
	public List<Category> getCategoryForLoyaltyProducts();
	

	/**
	 * @param categoryName
	 * @return
	 */
	public List<LoyaltyProduct> getProductsByCategoryName(String categoryName);
}
