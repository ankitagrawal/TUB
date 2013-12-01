package com.hk.admin.pact.service.catalog.product;

import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductVariantSupplierInfoService {

	public ProductVariantSupplierInfo updatePVSupplierInfo(ProductVariantSupplierInfo productVariantSupplierInfo, Long askedQty, Long receivedQty);

	public ProductVariantSupplierInfo getOrCreatePVSupplierInfo(ProductVariant productVariant, Supplier supplier);
	
	public Supplier getSupplierFromProductVariant(ProductVariant productVariant);

}
