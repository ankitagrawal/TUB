package com.hk.loyaltypg.dao.impl;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.category.Category;
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
		return list.get(0);
	}

	@Override
	public List<Category> getCategoryForLoyaltyProducts() {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "prodVariant", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("prodVariant.product", "prod", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("prod.categories", "category", CriteriaSpecification.LEFT_JOIN);
		
		ProjectionList projectionsList = Projections.projectionList();
		projectionsList.add(Projections.alias(Projections.property("category.name"), "name"));
		projectionsList.add(Projections.alias(Projections.property("category.displayName"), "displayName"));

		criteria.setProjection(Projections.distinct(projectionsList));
		criteria.setResultTransformer(Transformers.aliasToBean(Category.class));
		
		@SuppressWarnings("unchecked")
		List<Category> list = this.findByCriteria(criteria);
		return list;
	}

}
