package com.hk.admin.pact.dao.catalog.product;

import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductVariantSupplierInfoDao extends BaseDao {

	public ProductVariantSupplierInfo getPVSupplierInfoByVariantAndSupplier(ProductVariant productVariant, Supplier supplier);
	
	public Supplier getSupplierFromProductVariant(ProductVariant productVariant);

}
