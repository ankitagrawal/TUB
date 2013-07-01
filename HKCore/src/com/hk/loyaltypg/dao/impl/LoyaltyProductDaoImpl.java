package com.hk.loyaltypg.dao.impl;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.loyaltypg.dao.LoyaltyProductDao;
import com.hk.store.CategoryDto;

@Repository
public class LoyaltyProductDaoImpl extends BaseDaoImpl implements LoyaltyProductDao {

	@Override
	public LoyaltyProduct getProductbyVariantId(String variantId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.add(Restrictions.eq("variant.id", variantId));
		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> list = this.findByCriteria(criteria);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<CategoryDto> getCategoryDtoForLoyaltyProducts() {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "prodVariant", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("prodVariant.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("prodVariant.deleted", Boolean.FALSE));
		criteria.createAlias("prodVariant.product", "prod", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("prod.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("prod.deleted", Boolean.FALSE));
		criteria.createAlias("prod.categories", "category", CriteriaSpecification.INNER_JOIN);
		
		ProjectionList projectionsList = Projections.projectionList();
		projectionsList.add(Projections.alias(Projections.property("category.name"), "name"));
		projectionsList.add(Projections.alias(Projections.property("category.displayName"), "displayName"));
		projectionsList.add(Projections.alias(Projections.count("prod.id"), "prodCount"));
		projectionsList.add(Projections.groupProperty("category.name"));
		criteria.setProjection(projectionsList);
		criteria.addOrder(Order.asc("category.name"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(CategoryDto.class));
		
		@SuppressWarnings("unchecked")
		List<CategoryDto> list = this.findByCriteria(criteria);
		return list;
	}

	
	@Override
	public List<LoyaltyProduct> getProductsByCategoryName(String categoryName) {

		DetachedCriteria criteria = DetachedCriteria.forClass(LoyaltyProduct.class);
		criteria.createAlias("variant", "prodVariant", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("prodVariant.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("prodVariant.deleted", Boolean.FALSE));
		criteria.createAlias("prodVariant.product", "prod", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("prod.outOfStock", Boolean.FALSE));
		criteria.add(Restrictions.eq("prod.deleted", Boolean.FALSE));
		criteria.createAlias("prod.categories", "category", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("category.name", categoryName));

		@SuppressWarnings("unchecked")
		List<LoyaltyProduct> productList = this.findByCriteria(criteria);
		return productList;
	}

}
