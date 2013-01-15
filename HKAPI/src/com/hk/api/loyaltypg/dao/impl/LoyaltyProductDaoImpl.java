package com.hk.api.loyaltypg.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hk.api.loyaltypg.dao.LoyaltyProductDao;
import com.hk.api.loyaltypg.domain.LoyaltyProduct;
import com.hk.impl.dao.BaseDaoImpl;

public class LoyaltyProductDaoImpl extends BaseDaoImpl implements LoyaltyProductDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<LoyaltyProduct> listProductsUnderKarmaPints(int karmaPoints, int startRow, int maxRows) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.add(Restrictions.le("points", karmaPoints));
		return findByCriteria(criteria, startRow, maxRows);
	}
	
}
