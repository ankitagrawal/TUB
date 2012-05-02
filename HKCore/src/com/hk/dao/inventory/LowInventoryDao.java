package com.hk.dao.inventory;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.LowInventory;

@SuppressWarnings("unchecked")
@Repository
public class LowInventoryDao extends BaseDaoImpl {

    public LowInventory findLowInventory(ProductVariant productVariant) {
        return (LowInventory) getSession().createQuery("from LowInventory li where li.productVariant = :productVariant").setParameter("productVariant", productVariant).uniqueResult();
    }

    
    public Page findLowInventoryList(Product product, ProductVariant productVariant, Category category, String brandName, int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(LowInventory.class);
        if (productVariant != null) {
            criteria.add(Restrictions.eq("productVariant", productVariant));
        }
        DetachedCriteria productVariantCriteria = criteria.createCriteria("productVariant");
        if (product != null)
            productVariantCriteria.add(Restrictions.eq("product", product));
        if (category != null || brandName != null) {
            DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product");
            if (category != null)
                productCriteria.add(Restrictions.eq("primaryCategory", category));
            if (brandName != null)
                productCriteria.add(Restrictions.eq("brand", brandName));
        }

        criteria.addOrder(org.hibernate.criterion.Order.desc("entryDate"));
        return list(criteria, pageNo, perPage);

    }

    public Page findOutOfstockInventory(Product product, ProductVariant productVariant, Category category, String brandName, int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(LowInventory.class);
        criteria.add(Restrictions.eq("outOfStock", true));
        if (productVariant != null)
            criteria.add(Restrictions.eq("productVariant", productVariant));
        DetachedCriteria productVariantCriteria = criteria.createCriteria("productVariant");
        if (product != null)
            productVariantCriteria.add(Restrictions.eq("product", product));
        if (category != null || brandName != null) {
            DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product");
            if (category != null)
                productCriteria.add(Restrictions.eq("primaryCategory", category));
            if (brandName != null)
                productCriteria.add(Restrictions.eq("brand", brandName));
        }
        criteria.addOrder(org.hibernate.criterion.Order.desc("entryDate"));
        return list(criteria, pageNo, perPage);

    }

    // public List<LowInventory> findOutOfstockInventory() {
    // return getSession().
    // createQuery("from LowInventory li where li.outOfStock = :outOfStock").
    // setParameter("outOfStock", true).
    // list();
    //
    // }
    //

    public void deleteFromLowInventoryList(ProductVariant productVariant) {
        getSession().createQuery("delete from LowInventory li where li.productVariant = :productVariant").setParameter("productVariant", productVariant).executeUpdate();
    }

}
