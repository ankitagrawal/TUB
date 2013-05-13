package com.hk.impl.dao.catalog.product;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.ProductVariantDao;

@SuppressWarnings("unchecked")
@Repository
public class ProductVariantDaoImpl extends BaseDaoImpl implements ProductVariantDao {

    public ProductVariant getVariantById(String variantId) {
        return get(ProductVariant.class, variantId);
    }

    /*
     * public ProductVariantPaymentType getVariantPaymentType(EnumProductVariantPaymentType
     * enumProductVariantPaymentType){ return get(ProductVariantPaymentType.class,
     * enumProductVariantPaymentType.getId()); }
     */

    public List<ProductVariant> findVariantsFromFreeVariant(ProductVariant freeProductVariant) {
        return getSession().createQuery("from ProductVariant p where p.freeProductVariant = :freeProductVariant").setParameter("freeProductVariant", freeProductVariant).list();
    }

    public Set<ProductVariant> getProductVariantsFromProductVariantIds(String productVariantIds) {
        Set<ProductVariant> productVariants = new HashSet<ProductVariant>();

        String[] variantIdArr = productVariantIds.split(",");
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductVariant.class);
        criteria.add(Restrictions.in("id", Arrays.asList(variantIdArr)));

        List<ProductVariant> variantList = findByCriteria(criteria);
        productVariants.addAll(variantList);

        /*
         * for (String pvId : productVariantIds.split(",")) { ProductVariant productVariant = find(pvId.trim()); if
         * (productVariant != null) productVariants.add(productVariant); }
         */

        return productVariants;
    }

    public List<ProductVariant> getProductVariantsByProductId(String productId) {

        return (List<ProductVariant>) getSession().createQuery("from ProductVariant p where p.product.id = :productId").setParameter("productId", productId).list();

    }

    public void findAndSetBlankAffiliateCategory(AffiliateCategory affiliateCategory) {
        List<ProductVariant> toBeReset = getSession().createQuery("from ProductVariant p where p.affiliateCategory = :affiliateCategory").setEntity("affiliateCategory",
                affiliateCategory).list();
        for (ProductVariant productVariant : toBeReset) {
            productVariant.setAffiliateCategory(null);
        }
    }

    public ProductVariant findVariantFromUPC(String upc) {
        return (ProductVariant) getSession().createQuery("from ProductVariant p where p.upc = :upc").setParameter("upc", upc).uniqueResult();
    }

    public List<ProductVariant> findVariantListFromUPC(String upc) {
        return (List<ProductVariant>) getSession().createQuery("from ProductVariant p where p.upc = :upc").setParameter("upc", upc).list();
    }

    // to debug mulitple PV with same upc, ideally should return 1 only
    public List<ProductVariant> findVariantsFromUPC(String upc) {
        return getSession().createQuery("from ProductVariant p where p.upc = :upc").setParameter("upc", upc).list();
    }

    // find by upc, if null, find by variant
    public List<ProductVariant> findVariantFromBarcode(String barcode) {
        List<ProductVariant> productVariants;
        Criteria criteria = getSession().createCriteria(ProductVariant.class);
        criteria.add(Restrictions.eq("upc", barcode));
        productVariants = criteria.list();
        if (productVariants != null && productVariants.size() > 0) {
            return productVariants;
        } else {
            Criteria criteria2 = getSession().createCriteria(ProductVariant.class);
            criteria2.add(Restrictions.eq("id", barcode));
            return criteria2.list();
        }
    }

    public Long findNetInventory(ProductVariant productVariant) {
        return (Long) getSession().createQuery("select sum(p.qty) from ProductVariantInventory p where p.sku.productVariant = :productVariant").setParameter("productVariant",
                productVariant).uniqueResult();
    }

    @Transactional
    public ProductVariant save(ProductVariant productVariant) {
        if (productVariant.getCreatedDate() == null) {
            productVariant.setCreatedDate(BaseUtils.getCurrentTimestamp());
        }
        return (ProductVariant) super.save(productVariant);
    }

    /**
     * returns list of all the variants irrespective of whether they are deleted or not.
     *
     * @param category
     * @return
     */
    public List<ProductVariant> getAllVariantsByCategory(String category) {
        return getSession().createQuery("select pv from ProductVariant pv left join pv.product.categories c where c.name = :category order by pv.orderRanking asc").setString(
                "category", category).list();
    }

    /**
     * returns list of all the variants which are not deleted.
     *
     * @param category
     * @return
     */
    public List<ProductVariant> getAllProductVariantsByCategory(String category) {
        return getSession().createQuery(
                "select pv from ProductVariant pv left join pv.product.categories c where c.name = :category and pv.deleted != 1 order by pv.orderRanking asc").setString(
                "category", category).list();
    }
    
    
    @Transactional
    public void markProductVariantsAsDeleted(Product product) {
    	String productId = product.getId();
    	List<ProductVariant> variants = getProductVariantsByProductId(productId);
		if (variants != null) {
			for (ProductVariant productVariant : variants) {
				productVariant.setDeleted(true);
				super.save(productVariant);
			}
		}
    }

    public List<ProductVariant> getAllProductVariant() {
        return getAll(ProductVariant.class);
    }

	public List<ProductVariant> getAllNonDeletedProductVariant() {
        return getSession().createQuery(
                "select pv from ProductVariant pv where pv.product.deleted != 1 and pv.deleted != 1").list();
    }

    public List<ProductVariant> getVariantsForQuickInventoryCheck(){
        return getSession().createQuery(
                "select pv from ProductVariant pv where pv.product.deleted != 1 and pv.deleted != 1 " +
                        "and (pv.outOfStock = 1 or pv.product.outOfStock = 1)" +
                        " and pv.product.hidden != 1 and pv.product.isJit != 1").list();
    }

    public Product getProductForProudctVariant(String variantId) {
        return getVariantById(variantId).getProduct();  //To change body of implemented methods use File | Settings | File Templates.

    }
}