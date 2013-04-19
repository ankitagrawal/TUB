package com.hk.loyaltypg.dao;

import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.pact.dao.BaseDao;

public interface LoyaltyProductDao extends BaseDao {

	LoyaltyProduct getProductbyVarientId(String variantId);
	
}
