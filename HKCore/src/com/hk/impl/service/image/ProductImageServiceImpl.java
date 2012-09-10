package com.hk.impl.service.image;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.image.ProductImageDao;
import com.hk.pact.service.image.ProductImageService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 9/10/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductImageServiceImpl implements ProductImageService {

	ProductImageDao productImageDao;

	@Override
	public List<ProductImage> searchProductImages(Long imageTypeId, Product product, ProductVariant productVariant, boolean fetchVariantImages, boolean fetchHiddenImages) {
		return productImageDao.searchProductImages(imageTypeId, product, productVariant, fetchVariantImages, fetchHiddenImages);
	}
}
