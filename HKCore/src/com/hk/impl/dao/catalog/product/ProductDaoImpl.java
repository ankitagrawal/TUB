package com.hk.impl.dao.catalog.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.domain.catalog.product.combo.Combo;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.service.ServiceLocatorFactory;

@SuppressWarnings("unchecked")
@Repository
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {

    public Product getProductById(String productId) {
        return get(Product.class, productId);
    }

    public Product getOriginalProductById(String productId) {
      SessionFactory sessionFactory = (SessionFactory)ServiceLocatorFactory.getService("newSessionFactory") ;
      Session session = sessionFactory.openSession();
      Product product = (Product) session.createQuery(
                "select p from Product p where p.id=:productId").setString(
                "productId", productId).uniqueResult();
      if(product != null)
        product.setCategoriesPipeSeparated(product.getPipeSeparatedCategories());
      return product;
    }

    @Transactional
    public Product save(Product product) {
        if (product.getDeleted() == null) {
            product.setDeleted(Boolean.FALSE);
        }
        if (product.getService() == null) {
            product.setService(Boolean.FALSE);
        }
        if (product.getGoogleAdDisallowed() == null) {
            product.setGoogleAdDisallowed(Boolean.FALSE);
        }
        if (product.getJit() == null)   {
            product.setJit(Boolean.FALSE);
        }
	    if (product.getCodAllowed() == null)   {
            product.setCodAllowed(Boolean.FALSE);
        }
	    if (product.getOutOfStock() == null)   {
            product.setOutOfStock(Boolean.FALSE);
        }
	    if (product.isHidden() == null)   {
            product.setHidden(Boolean.FALSE);
        }
        return (Product) super.save(product);
    }

    public List<Product> getProductByCategory(String category) {
        return getSession().createQuery(
                "select p from Product p left join p.categories c where c.name = :category and p.deleted != :deleted and p.isGoogleAdDisallowed != :isGoogleAdDisallowed order by p.orderRanking asc").setString(
                "category", category).setBoolean("deleted", true).setBoolean("isGoogleAdDisallowed", true).list();
    }

    public List<Product> getProductByCategory(List<String> category) {
        return getSession().createQuery(
                "select p from Product p left join p.categories c where c.name in (:category) and p.deleted != :deleted and p.isGoogleAdDisallowed != :isGoogleAdDisallowed order by p.orderRanking asc").setParameterList(
                "category", category).setBoolean("deleted", true).setBoolean("isGoogleAdDisallowed", true).list();
    }

    public List<Product> getProductByCategories(List<String> categoryNames) {
        return getSession().createQuery("select p from Product p where p.primaryCategory.name in (:category) and deleted = :nonDeleted and isGoogleAdDisallowed != :adAllowed order by p.orderRanking asc")
                .setParameterList("category", categoryNames)
                .setParameter("adAllowed",true)
                .setParameter("nonDeleted", false).list();
    }

    public List<Product> getOOSHiddenDeletedProducts(){
       return  (List<Product>) findByNamedParams(" from Product p where p.hidden =:hidden or p.deleted =:deleted or p.outOfStock =:outOfStock", new String[]{"hidden","deleted","outOfStock"}, new Object[]{true, true, true});
    }

    /**
     * returns list of all the products irrespective of whether they are deleted or not.
     *
     * @param category
     * @return
     */
    public List<Product> getAllProductByCategory(String category) {
        return getSession().createQuery("select p from Product p where p.primaryCategory.name = :category order by p.orderRanking asc").setString("category", category).list();
    }


    /**
     * returns list of all products irrespective of category
     * @param
     * @return
     */
    public List<Product> getAllProductsForCatalog() {
        return getSession().createQuery("select p from Product p order by p.primaryCategory.name asc, p.orderRanking asc ").list();
    }
    public List<Product> getAllSubscribableProductsByCategory(String category){
        return getSession().createQuery("select p from Product p where p.primaryCategory.name = :category and isSubscribable=true order by p.orderRanking asc").setString("category", category).list();
    }

    public List<Product> getAllProductNotByCategory(List<String> categoryNames) {
        return getSession().createQuery("select p from Product p where p.primaryCategory.name not in (:category) and deleted = :nonDeleted and isGoogleAdDisallowed != :adAllowed order by p.orderRanking asc")
                .setParameterList("category", categoryNames)
                .setParameter("adAllowed",true)
                .setParameter("nonDeleted", false).list();
    }

    /**
     * checks if a brand name exists or not
     *
     * @param brandName
     * @return
     */
    public boolean doesBrandExist(String brandName) {
        Long count = (Long) (getSession().createQuery("select count(distinct p.brand) from Product p where p.brand = :brandName").setString("brandName", brandName).uniqueResult());
        return (count != null && count > 0);
    }

    public List<Product> getAllProductBySubCategory(String category) {
        return getSession().createQuery("select p from Product p left join p.categories c where c.name = :category order by p.orderRanking asc").setString("category", category).list();
    }


    public List<Product> getAllNonDeletedProducts() {
        return super.findByQuery("select p from Product p where p.deleted = false");
    }

    public List<Product> getAllNonDeletedProductsWithImages() {
        return super.findByQuery("select p from Product p where p.deleted = false and p.mainImageId is not null");
    }

    public List<Product> getAllProductByBrand(String brand) {
        return getSession().createQuery("select p from Product p where p.brand = :brand order by p.orderRanking asc").setString("brand", brand).list();
    }

    public Page getAllProductsByCategoryAndBrand(String category, String brand, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        // Criteria criteria = getSession().createCriteria(Product.class);

        if (brand != null)
            criteria.add(Restrictions.eq("brand", brand));

        if (category != null) {
            DetachedCriteria categoryCriteria = criteria.createCriteria("categories");
            categoryCriteria.add(Restrictions.eq("name", category));
        }
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public Page getProductByBrand(String brand, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        // Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("brand", brand));
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return getSession().createQuery("select p from Product p left join p.categories c where c.name = :category and p.brand = :brand and p.deleted != :deleted").setString(
                "category", category).setString("brand", brand).setBoolean("deleted", true).list();
    }

    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        // Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("brand", brand));
        criteria.add(Restrictions.eq("deleted", false));
        DetachedCriteria categoryCriteria = criteria.createCriteria("categories");
        categoryCriteria.add(Restrictions.eq("name", category));
        criteria.addOrder(Order.asc("outOfStock"));
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

	public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand, Boolean onlyCOD, Boolean includeCombo, int page, int perPage) {
		if (categoryNames != null && categoryNames.size() > 0) {
			List<String> productIds = new ArrayList<String>();
			productIds = getSession().createQuery(
					"select p.id from Product p inner join p.categories c where c.name in (:categories) group by p.id having count(*) = :tagCount").setParameterList(
					"categories",
					categoryNames).setInteger("tagCount", categoryNames.size()).list();
			if (productIds != null && productIds.size() > 0) {
				if (!includeCombo) {
					productIds = getSession().createQuery(
							"select distinct pv.product.id from ProductVariant pv where pv.product.id in (:productIds)").setParameterList(
							"productIds",
							productIds).list();
				}

				if (productIds != null && productIds.size() > 0) {

					DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
					if (StringUtils.isNotBlank(brand)) {
						criteria.add(Restrictions.eq("brand", brand));
					}
					criteria.add(Restrictions.in("id", productIds));
					criteria.add(Restrictions.eq("deleted", false));
					criteria.add(Restrictions.eq("isGoogleAdDisallowed", false));
					criteria.add(Restrictions.eq("hidden", false));
					if (onlyCOD) {
						criteria.add(Restrictions.eq("codAllowed", true));
					}

					criteria.addOrder(Order.asc("outOfStock"));
					criteria.addOrder(Order.asc("orderRanking"));

					return list(criteria, page, perPage);
				}
			}
		}
		return null;
	}

	public Page getProductByCategoryBrandAndOptions(List<String> categoryNames, String brand, List<Long> filterOptions, int groupsCount, Double minPrice, Double maxPrice,
                                                    Boolean onlyCOD, Boolean includeCombo, int page, int perPage) {
		if (categoryNames != null && categoryNames.size() > 0) {
			List<String> productIds = new ArrayList<String>();
            if (includeCombo){
                productIds = getSession().createQuery("select p.id from Product p inner join p.categories c where c.name in (:categories) and p.deleted <> 1 group by p.id having count(*) = :tagCount").setParameterList("categories", categoryNames).setInteger("tagCount", categoryNames.size()).list();
            }else{
                productIds = getSession().createQuery("select  distinct pv.product.id from ProductVariant pv inner join pv.product.categories c where c.name in (:categories) and pv.product.deleted <> 1 group by pv.product.id having count(*) = :tagCount").setParameterList("categories", categoryNames).setInteger("tagCount", categoryNames.size()).list();
            }

			if (productIds != null && !productIds.isEmpty()) {
				productIds = getSession().createQuery("select pv.product.id from ProductVariant pv where pv.product.id in (:productIds) and pv.hkPrice between :minPrice and :maxPrice and pv.deleted <> 1").setParameterList("productIds", productIds).setParameter("minPrice", minPrice).setParameter("maxPrice", maxPrice).list();
				if (productIds != null && !productIds.isEmpty() && filterOptions != null && !filterOptions.isEmpty() && groupsCount > 0) {
					productIds = getSession().createSQLQuery("select distinct pv.product_id from product_variant_has_product_option pvhpo, product_variant pv where pvhpo.product_variant_id=pv.id and pv.product_id in (:productIds) and pvhpo.product_option_id in (:filterOptions) group by pvhpo.product_variant_id having count(pvhpo.product_variant_id) = :groupsCount").setParameterList("productIds", productIds).setParameterList("filterOptions", filterOptions).setParameter("groupsCount", groupsCount).list();
				}
				if (productIds != null && !productIds.isEmpty()) {
					DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
					if (StringUtils.isNotBlank(brand)) {
						criteria.add(Restrictions.eq("brand", brand));
					}
					criteria.add(Restrictions.in("id", productIds));
					criteria.add(Restrictions.eq("deleted", false));
					criteria.add(Restrictions.eq("isGoogleAdDisallowed", false));
					criteria.add(Restrictions.eq("hidden", false));
          if (onlyCOD){
              criteria.add(Restrictions.eq("codAllowed", true));
          }
          criteria.addOrder(Order.asc("outOfStock"));
					criteria.addOrder(Order.asc("orderRanking"));
					return list(criteria, page, perPage);
				}
			}
		}
		return null;
	}

	// test code
    public Page getProductByCategoryAndBrandNew(Category cat1, Category cat2, Category cat3, String brand, int page, int perPage) {

        String q = "SELECT c.product_id FROM category_has_product c WHERE c.category_name =\"" + cat1.getName() + ""
                + "\" AND product_id IN (SELECT c.product_id FROM category_has_product c WHERE c.category_name =\"" + cat2.getName()
                + "\" AND product_id IN (SELECT c.product_id FROM category_has_product c WHERE c.category_name =\"" + cat3.getName() + "\"))";

        List<String> productIds = getSession().createSQLQuery(q).list();

        if (productIds != null && productIds.size() > 0) {

            DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
            if (StringUtils.isNotBlank(brand)) {
                criteria.add(Restrictions.eq("brand", brand));
            }
            criteria.add(Restrictions.in("id", productIds));
            criteria.add(Restrictions.eq("deleted", false));
            criteria.addOrder(Order.asc("orderRanking"));

            return list(criteria, page, perPage);
        }
        return null;
    }

    public List<Product> getProductByName(String name) {
        return getSession().createQuery("select p from Product p where p.name like :name and p.deleted != :deleted").setString("name", "%" + name + "%").setBoolean("deleted", true).list();
    }

    public Page getProductByName(String name, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.like("name", "%" + name + "%"));
        criteria.add(Restrictions.eq("deleted", false));
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public Page getProductByName(String name,boolean onlyCOD, boolean includeCombo, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.eq("deleted", false));
        if (onlyCOD){
            criteria.add(Restrictions.eq("codAllowed", true));
        }
        if (!includeCombo){
            List<String> productIds =  getSession().createQuery("select distinct pv.product.id from ProductVariant pv where pv.product.name like (:name) group by pv.product.id")
                    .setString("name","%" + name + "%").list();
            criteria.add(Restrictions.in("id", productIds));
        }else{
            criteria.add(Restrictions.like("name", "%" + name + "%"));
        }
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public List<Product> getRelatedProducts(Product product) {
        // List<Category> categoryList = new ArrayList<Category>();
        // Category lowestLevelCategory = product.getLowestLevelCategory();
        // if (lowestLevelCategory != null) {
        // categoryList.add(lowestLevelCategory);
        // }
        // categoryList.add(productManager.getTopLevelCategory(product));
        // return getSession().createQuery("select distinct p from Product p left join p.categories c where c in
        // (:categories) and p <> :currentProduct and p.deleted != :deleted and p.isGoogleAdDisallowed !=
        // :isGoogleAdDisallowed order by p.orderRanking asc ")
        // .setParameterList("categories", categoryList)
        // .setEntity("currentProduct", product)
        // .setBoolean("deleted", true)
        // .setBoolean("isGoogleAdDisallowed", true)
        // .setMaxResults(4)
        // .list();

        return new ArrayList<Product>();
    }

    public Set<Product> getProductsFromProductIds(String productIds) {
        Set<Product> products = new HashSet<Product>();

        String[] productIdArr = productIds.split(",");
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.in("id", Arrays.asList(productIdArr)));

        List<Product> productList = findByCriteria(criteria);
        products.addAll(productList);

        /*
         * for (String productId : productIds.split(",")) { Product product = find(productId.trim()); if (product !=
         * null) products.add(product); }
         */

        return products;

    }

    public List<Product> getAllProductsById(List<String> productIdList) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.in("id", productIdList));
        return findByCriteria(criteria);
    }

    public Page getPaginatedResults(List<String> productIdList, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.in("id", productIdList));
        return list(criteria, page, perPage);
    }

    public List<Product> getRecentlyAddedProducts() {
        String query = "select p from Product p where p.deleted != :deleted and p.createDate != null order by p.createDate desc";
        return getSession().createQuery(query).setBoolean("deleted", true).list();
    }

    public ProductImage getProductImageByChecksum(String checksum) {
        return (ProductImage) getSession().createQuery("from ProductImage pi where pi.checksum = :checksum").setString("checksum", checksum).uniqueResult();
    }

    public List<ProductImage> getImagesByProductForProductMainPage(Product product) {
        return getSession().createQuery("from ProductImage pi where pi.product = :product and pi.productVariant is null and pi.hidden != :isHidden").setParameter("product",
                product)
                // .setParameter("productMainImageId", product.getMainImageId())
                .setBoolean("isHidden", true).list();
    }

    @SuppressWarnings("unchecked")
    public ProductExtraOption findProductExtraOptionByNameAndValue(String name, String value) {
        name = StringUtils.strip(name);
        value = StringUtils.strip(value);
        List<ProductExtraOption> extraOptionList = getSession().createQuery("from ProductExtraOption pxo where pxo.name = :name and pxo.value = :value").setString("name", name).setString(
                "value", value).list();
        return extraOptionList != null && extraOptionList.size() > 0 ? extraOptionList.get(0) : null;
    }

    public ProductGroup findProductGroupByName(String name) {
        return (ProductGroup) getSession().createQuery("from ProductGroup pg where pg.name=:name").setString("name", name).uniqueResult();
    }

    public ProductOption findProductOptionByNameAndValue(String name, String value) {
        name = StringUtils.strip(name);
        value = StringUtils.strip(value);
        List<ProductOption> optionList = getSession().createQuery("from ProductOption po where po.name = :name and po.value = :value").setString("name", name).setString("value",
                value).list();
        return optionList != null && optionList.size() > 0 ? optionList.get(0) : null;
    }
	
	public List<ProductOption> getProductOptions(List<Long> options) {
        return getSession().createQuery("from ProductOption po where po.id in(:options) order by upper(po.name), po.value asc").setParameterList("options", options).list();
    }

}
