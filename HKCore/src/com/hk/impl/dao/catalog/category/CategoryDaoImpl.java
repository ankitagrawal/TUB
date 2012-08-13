package com.hk.impl.dao.catalog.category;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public List<ProductOptionDto> getProductOptions(String category) {
		String queryString = "select po.id as id, upper(po.name) as name, po.value as value, count(po.id) as qty from ProductVariant pv inner join pv.productOptions po inner join pv.product.categories c " + "where c.name = :category and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1 group by po.id order by po.name asc ";
		Query query = getSession().createQuery(queryString).setParameter("category", category).setCacheable(true);
		query.setResultTransformer(Transformers.aliasToBean(ProductOptionDto.class)).list();
		return query.list();
	}

}
