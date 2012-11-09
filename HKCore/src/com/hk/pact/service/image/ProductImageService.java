package com.hk.pact.service.image;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 9/10/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductImageService {

	public List<ProductImage> searchProductImages(Long imageTypeId, Product product, ProductVariant productVariant, boolean fetchVariantImages,Boolean fetchHiddenImages);


}
