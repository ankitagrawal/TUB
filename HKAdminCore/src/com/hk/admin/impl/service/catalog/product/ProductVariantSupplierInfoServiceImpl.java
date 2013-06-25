package com.hk.admin.impl.service.catalog.product;

import com.hk.admin.pact.dao.catalog.product.ProductVariantSupplierInfoDao;
import com.hk.admin.pact.service.catalog.product.ProductVariantSupplierInfoService;
import com.hk.domain.catalog.ProductVariantSupplierInfo;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProductVariantSupplierInfoServiceImpl implements ProductVariantSupplierInfoService {

	@Autowired
	ProductVariantSupplierInfoDao productVariantSupplierInfoDao;

	public ProductVariantSupplierInfo updatePVSupplierInfo(ProductVariantSupplierInfo productVariantSupplierInfo, Long askedQty, Long receivedQty) {

		if (productVariantSupplierInfo != null) {
			if(askedQty != null) {
				productVariantSupplierInfo.setAskedQty(productVariantSupplierInfo.getAskedQty() + askedQty);
			}

			if(receivedQty != null) {
				productVariantSupplierInfo.setReceivedQty(productVariantSupplierInfo.getReceivedQty() + receivedQty);
			}

			if(productVariantSupplierInfo.getAskedQty() > 0) {
				productVariantSupplierInfo.setFillRate((productVariantSupplierInfo.getReceivedQty() * 100.0) / productVariantSupplierInfo.getAskedQty() );
			}

			getProductVariantSupplierInfoDao().save(productVariantSupplierInfo);
		}
		return productVariantSupplierInfo;
	}

	public ProductVariantSupplierInfo getOrCreatePVSupplierInfo(ProductVariant productVariant, Supplier supplier) {
		ProductVariantSupplierInfo productVariantSupplierInfo = getProductVariantSupplierInfoDao().getPVSupplierInfoByVariantAndSupplier(productVariant, supplier);

		if (productVariantSupplierInfo == null) {
			productVariantSupplierInfo = new ProductVariantSupplierInfo();
			productVariantSupplierInfo.setProductVariant(productVariant);
			productVariantSupplierInfo.setSupplier(supplier);
			productVariantSupplierInfo = (ProductVariantSupplierInfo)getProductVariantSupplierInfoDao().save(productVariantSupplierInfo);
		}
		return productVariantSupplierInfo;
	}
	
	public Supplier getSupplierFromProductVariant(String productVariant) {
		Supplier supplier = getProductVariantSupplierInfoDao().getSupplierFromProductVariant(productVariant);
		return supplier;
	}

	public ProductVariantSupplierInfoDao getProductVariantSupplierInfoDao() {
		return productVariantSupplierInfoDao;
	}

	public void setProductVariantSupplierInfoDao(ProductVariantSupplierInfoDao productVariantSupplierInfoDao) {
		this.productVariantSupplierInfoDao = productVariantSupplierInfoDao;
	}
}
