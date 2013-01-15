package com.hk.api.loyaltypg.dao;

import java.util.List;

import com.hk.api.loyaltypg.domain.LoyaltyProduct;
import com.hk.pact.dao.BaseDao;

public interface LoyaltyProductDao extends BaseDao {
	
	List<LoyaltyProduct> listProductsUnderKarmaPints(int karmaPoints, int startRow, int maxRows);

}
