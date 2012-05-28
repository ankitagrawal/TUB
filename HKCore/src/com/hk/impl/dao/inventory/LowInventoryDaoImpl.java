package com.hk.impl.dao.inventory;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.LowInventory;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.inventory.LowInventoryDao;

@SuppressWarnings("unchecked")
@Repository
public class LowInventoryDaoImpl extends BaseDaoImpl implements LowInventoryDao {

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

/*    public void deleteFromLowInventoryList(ProductVariant productVariant) {
        ProductVariantInventory pvi = (ProductVariantInventory) findUniqueByNamedParams("from LowInventory li where li.productVariant = :productVariant ",
                new String[] { "productVariant" }, new Object[] { productVariant });

        if (pvi != null) {
            delete(pvi);
        }
        //getSession().createQuery("delete ").setParameter("productVariant", productVariant).executeUpdate();
    }*/
	
	    public void deleteFromLowInventoryList(ProductVariant productVariant) {
        LowInventory li = (LowInventory) findUniqueByNamedParams("select li from LowInventory li where li.productVariant = :productVariant ",
                new String[] { "productVariant" }, new Object[] { productVariant });

        if (li != null) {
            delete(li);
        }
        //getSession().createQuery("delete ").setParameter("productVariant", productVariant).executeUpdate();
    }


}
