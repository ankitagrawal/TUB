package com.hk.pact.dao.catalog.product;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.catalog.product.ProductVariant;

public interface UpdatePvPriceDao {

	Page getPVForPriceUpdate(Category primaryCategory, ProductVariant productVariant, Long status, int pageNo, int perPage);

	UpdatePvPrice getPVForPriceUpdate(ProductVariant productVariant, Long status);

	boolean isBrandAudited(String brand);
}