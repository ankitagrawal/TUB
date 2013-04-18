package com.hk.loyaltypg.dao;

import java.util.List;

import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.loyaltypg.dto.CategoryLoyaltyDto;
import com.hk.pact.dao.BaseDao;

public interface LoyaltyProductDao extends BaseDao {

	LoyaltyProduct getProductbyVarientId(String variantId);
	
	
	/**
	 * @param categoryName
	 * @return
	 */
	public List<LoyaltyProduct> getProductsByCategoryName(String categoryName);
	
	/**
	 * Test method.
	 * @return
	 */
	public List<CategoryLoyaltyDto> getCategoryDtoForLoyaltyProducts() ;
}
