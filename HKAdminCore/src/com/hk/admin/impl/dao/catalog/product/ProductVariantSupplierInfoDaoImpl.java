package com.hk.admin.impl.dao.catalog.product;

import com.hk.admin.pact.dao.catalog.product.ProductVariantSupplierInfoDao;
import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ProductVariantSupplierInfoDaoImpl extends BaseDaoImpl implements ProductVariantSupplierInfoDao {

	public ProductVariantSupplierInfo getPVSupplierInfoByVariantAndSupplier(ProductVariant productVariant, Supplier supplier) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductVariantSupplierInfo.class);
		criteria.add(Restrictions.eq("productVariant", productVariant));
		criteria.add(Restrictions.eq("supplier", supplier));
		return (ProductVariantSupplierInfo)findByCriteria(criteria).get(0);
	}
}
