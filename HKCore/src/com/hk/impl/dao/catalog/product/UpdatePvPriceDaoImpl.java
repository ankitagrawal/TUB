package com.hk.impl.dao.catalog.product;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UpdatePvPriceDaoImpl extends BaseDaoImpl implements UpdatePvPriceDao {

	public Page getPVForPriceUpdate(Category primaryCategory, boolean updated, int pageNo, int perPage) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UpdatePvPrice.class);
		criteria.add(Restrictions.eq("updated", updated));
		if (primaryCategory != null) {
			DetachedCriteria productVariantCriteria = criteria.createCriteria("productVariant");
			DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product");
			productCriteria.add(Restrictions.eq("primaryCategory", primaryCategory));
		}

		criteria.addOrder(org.hibernate.criterion.Order.desc("txnDate"));
		return list(criteria, pageNo, perPage);
	}

}