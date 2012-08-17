package com.hk.impl.dao.catalog.category;

import java.util.*;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.dto.ProductOptionDto;
import com.hk.dto.PriceRangeDto;

@SuppressWarnings("unchecked")
@Repository
public class CategoryDaoImpl extends BaseDaoImpl implements CategoryDao{


    public List<String> getBrandsByCategory(List<String> categoryNames) {
        String queryString = "select distinct p.brand from Product p inner join p.categories c where c.name in (:categories) and p.deleted=:deleted  group by p.id having count(*) = :tagCount order by p.brand asc";
        return findByNamedParams(queryString, new String[] { "categories", "deleted", "tagCount" }, new Object[] { categoryNames, false, new Long(categoryNames.size()) });
    }

    public List<Category> getCategoriesByBrand(String brand, String topLevelCategory) {
        String queryString = "select distinct p.categories from Product p inner join p.categories c where c.name in (:topLevelCategory) and p.brand in (:brand) and p.deleted=:deleted";
        return findByNamedParams(queryString, new String[] { "brand", "topLevelCategory", "deleted" }, new Object[] { brand, topLevelCategory, false });
    }

    public Set<Category> getCategoriesFromCategoryNames(String categoryNames) {
        Set<Category> categories = new HashSet<Category>();

        String[] categoryNamesArr = categoryNames.split(",");
        DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
        criteria.add(Restrictions.in("name", Arrays.asList(categoryNamesArr)));

        List<Category> categoryList = findByCriteria(criteria);
        categories.addAll(categoryList);

        return categories;
    }

    public Category getCategoryByName(String name) {
        String queryString = "select c from Category c where c.name = ?";
        return (Category) findUnique(queryString, new Object[] { name });
    }

    public List<Category> getPrimaryCategories() {
        return findByQuery("select distinct p.primaryCategory from Product p");
    }

	public List<ProductOptionDto> getProductOptions(List<String> categoryNames, List<Long> filterOptions, int groupsCount, Double minPrice, Double maxPrice) {
		if (categoryNames != null && categoryNames.size() > 0) {
			String query1 = "select pv.product.id from ProductVariant pv inner join pv.product.categories c where c.name in (:categories) and pv.hkPrice between :min and :max group by pv.product.id having count(*) = :tagCount";
			List<String> productIds;
			Query queryObj = getSession().createQuery(query1).setParameterList("categories", categoryNames);
			queryObj = queryObj.setParameter("min", minPrice).setParameter("max", maxPrice).setInteger("tagCount", categoryNames.size());
			productIds = queryObj.list();
			logger.debug("productIds: " + productIds);if (productIds != null && !productIds.isEmpty()) {
				if (filterOptions != null && !filterOptions.isEmpty()) {
					String query2 = "select distinct pv.id from product_variant_has_product_option pvhpo, product_variant pv " + "where pvhpo.product_variant_id=pv.id and pvhpo.product_option_id in (:filterOptions) and pv.product_id in (:productIds) " + "group by pvhpo.product_variant_id having count(pvhpo.product_variant_id) = :groupsCount";
					List<String> pvIds = getSession().createSQLQuery(query2)
							.setParameterList("filterOptions", filterOptions)
							.setParameterList("productIds", productIds)
							.setInteger("groupsCount", groupsCount).list();
					if (pvIds != null && !pvIds.isEmpty()) {
						String queryString = "select po.id as id, upper(po.name) as name, po.value as value, count(po.id) as qty from ProductVariant pv inner join pv.productOptions po where pv.id in(:pvIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1 group by po.id order by po.name desc , po.value asc";
						Query query3 = getSession().createQuery(queryString).setParameterList("pvIds", pvIds);
						query3.setResultTransformer(Transformers.aliasToBean(ProductOptionDto.class)).list();
						return query3.list();
					} else
						return null;
				} else {
					String queryString = "select po.id as id, upper(po.name) as name, po.value as value, count(po.id) as qty from ProductVariant pv inner join pv.productOptions po where pv.product.id in(:productIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1 group by po.id order by po.name desc , po.value asc";
					Query query = getSession().createQuery(queryString).setParameterList("productIds", productIds);
					query.setResultTransformer(Transformers.aliasToBean(ProductOptionDto.class)).list();
					return query.list();
				}
			}
		}
		return null;
	}

	public PriceRangeDto getPriceRange(List<String> categoryNames, List<Long> filterOptions, int groupsCount) {
		if (categoryNames != null && categoryNames.size() > 0) {
			List<String> productIds = getSession().createQuery("select p.id from Product p inner join p.categories c where c.name in (:categories) group by p.id having count(*) = :tagCount").setParameterList("categories", categoryNames).setInteger("tagCount", categoryNames.size()).list();
			if (productIds != null && !productIds.isEmpty()) {
				/*if (filterOptions != null && !filterOptions.isEmpty()) {
					String query2 = "select distinct pv.id from product_variant_has_product_option pvhpo, product_variant pv " + "where pvhpo.product_variant_id=pv.id and pvhpo.product_option_id in (:filterOptions) and pv.product_id in (:productIds) " + "group by pvhpo.product_variant_id having count(pvhpo.product_variant_id) = :groupsCount";
					List<String> pvIds = getSession().createSQLQuery(query2).setParameterList("filterOptions", filterOptions).setParameterList("productIds", productIds).setInteger("groupsCount", groupsCount).list();
					if (pvIds != null && !pvIds.isEmpty()) {
						String queryString = "select min(pv.hkPrice) from ProductVariant pv where pv.id in (:pvIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1";
						Double minPrice = (Double) getSession().createQuery(queryString).setParameterList("pvIds", pvIds).uniqueResult();
						String queryString2 = "select max(pv.hkPrice) from ProductVariant pv where pv.id in (:pvIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1";
						Double maxPrice = (Double) getSession().createQuery(queryString2).setParameterList("pvIds", pvIds).uniqueResult();
						PriceRangeDto priceRangeDto = new PriceRangeDto(Math.floor(minPrice), Math.ceil(maxPrice));
						return priceRangeDto;
					}
				} else {*/
					String queryString = "select min(pv.hkPrice) from ProductVariant pv where pv.product.id in (:productIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1";
					Double minPrice = (Double) getSession().createQuery(queryString).setParameterList("productIds", productIds).uniqueResult();
					String queryString2 = "select max(pv.hkPrice) from ProductVariant pv where pv.product.id in (:productIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1";
					Double maxPrice = (Double) getSession().createQuery(queryString2).setParameterList("productIds", productIds).uniqueResult();
					PriceRangeDto priceRangeDto = new PriceRangeDto(Math.floor(minPrice), Math.ceil(maxPrice));
					return priceRangeDto;
				/*}*/
			}
		}
		return null;
	}
}
