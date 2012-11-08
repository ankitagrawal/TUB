package com.hk.pact.dao.image;

import com.hk.constants.catalog.image.EnumImageType;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductVariant;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 9/10/12
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductImageDao {

	public List<ProductImage> searchProductImages(Long imageTypeId, Product product, ProductVariant productVariant, boolean fetchAllImages, Boolean fetchHiddenImages);
}
