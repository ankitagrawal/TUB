package com.hk.admin.impl.dao.catalog.product;

import com.hk.admin.pact.dao.catalog.product.ProductVariantSupplierInfoDao;
import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

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
		List<ProductVariantSupplierInfo> productVariantSupplierInfoList = findByCriteria(criteria);
		if(productVariantSupplierInfoList != null && productVariantSupplierInfoList.size() > 0) {
			return productVariantSupplierInfoList.get(0);
		}
		return null;
	}

	@Override
	public Supplier getSupplierFromProductVariant(ProductVariant productVariant) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductVariantSupplierInfo.class);
		criteria.add(Restrictions.eq("productVariant", productVariant));
		List<ProductVariantSupplierInfo> productVariantSupplierInfoList = findByCriteria(criteria);
		if(productVariantSupplierInfoList!=null && productVariantSupplierInfoList.size()>0){
			Supplier supplier = productVariantSupplierInfoList.get(0).getSupplier();
			return supplier;
		}
		return null;
	}

}
