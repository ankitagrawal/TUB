package com.hk.impl.dao.catalog.category;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.category.Category;
import com.hk.dto.PriceRangeDto;
import com.hk.dto.ProductOptionDto;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.category.CategoryDao;

@SuppressWarnings("unchecked")
@Repository
public class CategoryDaoImpl extends BaseDaoImpl implements CategoryDao {

    private Logger logger = LoggerFactory.getLogger(CategoryDaoImpl.class);

    public List<String> getBrandsByCategory(List<String> categoryNames) {
        String queryString = "select distinct p.brand from Product p inner join p.categories c where c.name in (:categories) and p.deleted=:deleted  group by p.id having count(*) = :tagCount order by p.brand asc";
        return findByNamedParams(queryString, new String[] { "categories", "deleted", "tagCount" }, new Object[] { categoryNames, false, new Long(categoryNames.size()) });
    }

  public List<String> getBrandsByPrimaryCategory(Category primaryCategory) {
    String queryString = "select distinct p.brand from Product p where p.primaryCategory.name = :primaryCategory and p.deleted=:deleted and p.hidden <> 1 order by p.brand asc";
    List<String> allBrands = findByNamedParams(queryString, new String[]{"primaryCategory", "deleted"}, new Object[]{primaryCategory.getName(), false});

    /*queryString = "select distinct p.brand from Combo p where p.primaryCategory.name = :primaryCategory and p.deleted=:deleted and p.hidden <> 1 order by p.brand asc";
    List<String> comboBrands = findByNamedParams(queryString, new String[]{"primaryCategory", "deleted"}, new Object[]{primaryCategory.getName(), false});

    allBrands.removeAll(comboBrands);
*/    
    return allBrands;
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

    public List<Category> getCategoryByNames(List<String> categoryNames){
        String queryString = "from Category c where c.name in (:categoryNames)";
        return findByNamedParams(queryString, new String[]{"name"}, new Object[]{categoryNames});
    }


    public List<Category> getPrimaryCategories() {
        return findByQuery("select distinct p.primaryCategory from Product p where p.deleted<>1 and p.hidden<>1 order by p.primaryCategory.displayName asc");
    }

    public List<ProductOptionDto> getProductOptions(String primaryCategory, List<String> categoryNames, List<Long> filterOptions, int groupsCount, Double minPrice, Double maxPrice) {
        if (categoryNames != null && categoryNames.size() > 0) {
            List<String> productIds = getSession().createQuery(
                    "select p.id from Product p inner join p.categories c where c.name in (:categories) and p.deleted <> 1 and p.hidden <> 1 group by p.id having count(*) = :tagCount").setParameterList(
                    "categories", categoryNames).setInteger("tagCount", categoryNames.size()).list();
            if (productIds != null && !productIds.isEmpty()) {
                List<String> pvIds = getSession().createQuery(
                        "select pv.id from ProductVariant pv where pv.product.id in (:productIds) and pv.hkPrice between :minPrice and :maxPrice and pv.deleted <> 1").setParameterList(
                        "productIds", productIds).setParameter("minPrice", minPrice).setParameter("maxPrice", maxPrice).list();
                if (filterOptions != null && !filterOptions.isEmpty()) {
                    pvIds = getSession().createSQLQuery(
                            "select distinct pv.id from product_variant_has_product_option pvhpo, product_variant pv where pvhpo.product_variant_id=pv.id and pv.id in (:pvIds) and pvhpo.product_option_id in (:filterOptions) group by pvhpo.product_variant_id having count(pvhpo.product_variant_id) = :groupsCount").setParameterList(
                            "pvIds", pvIds).setParameterList("filterOptions", filterOptions).setParameter("groupsCount", groupsCount).list();
                }
                if (pvIds != null && !pvIds.isEmpty()) {
                    return getSession().createQuery(
                            "select po.id as id, upper(po.name) as name, po.value as value, count(distinct pv.product.id) as qty from ProductVariant pv inner join pv.productOptions po where pv.id in(:pvIds) group by po.id order by po.name desc , po.value asc").setParameterList(
                            "pvIds", pvIds).setResultTransformer(Transformers.aliasToBean(ProductOptionDto.class)).list();
                }
            }
        }
        return null;
    }

    public PriceRangeDto getPriceRange(String primaryCategory, List<String> categoryNames, List<Long> filterOptions, int groupsCount) {
        if (categoryNames != null && categoryNames.size() > 0) {
            List<String> productIds = getSession().createQuery(
                    "select p.id from Product p inner " + "join p.categories c where p.primaryCategory.name =:primaryCategory and c.name in (:categories) "
                            + "and p.deleted != :deleted group by p.id having count(*) = :tagCount ").setParameter("primaryCategory", primaryCategory).setBoolean("deleted", true).setParameterList("categories",
                    categoryNames).setInteger("tagCount", categoryNames.size()).list();

            if (productIds != null && !productIds.isEmpty()) {                
                String queryString = "select min(pv.hkPrice) from ProductVariant pv where pv.product.id in (:productIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1";
                Double minPrice = (Double) getSession().createQuery(queryString).setParameterList("productIds", productIds).uniqueResult();
                String queryString2 = "select max(pv.hkPrice) from ProductVariant pv where pv.product.id in (:productIds) and pv.product.deleted <> 1 and pv.deleted <> 1 and pv.outOfStock <> 1";
                Query query = getSession().createQuery(queryString2);

                Double maxPrice = (Double) query.setParameterList("productIds", productIds).uniqueResult();
                if(minPrice == null || maxPrice == null){
                    logger.error("null min or max price for" + productIds);
	                return new PriceRangeDto(0.0, 10000.0);
                }                
                return new PriceRangeDto(Math.floor(minPrice), Math.ceil(maxPrice));
            }
        }
        return new PriceRangeDto(0.0, 10000.0);
    }
}
