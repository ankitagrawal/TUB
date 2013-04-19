package com.hk.loyaltypg.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.loyaltypg.dao.LoyaltyProductDao;

@Repository
public class LoyaltyProductDaoImpl extends BaseDaoImpl implements LoyaltyProductDao {

	@Override
	public LoyaltyProduct getProductbyVarientId(String variantId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.add(Restrictions.eq("variant.id", variantId));
		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> list = this.findByCriteria(criteria);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
