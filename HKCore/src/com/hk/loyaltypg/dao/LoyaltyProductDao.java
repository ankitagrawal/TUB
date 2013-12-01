package com.hk.loyaltypg.dao;

import java.util.List;

import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.pact.dao.BaseDao;
import com.hk.store.CategoryDto;

public interface LoyaltyProductDao extends BaseDao {

	LoyaltyProduct getProductbyVariantId(String variantId);

	List<LoyaltyProduct> getProductsByCategoryName(String categoryName);

	List<CategoryDto> getCategoryDtoForLoyaltyProducts();
	
}
