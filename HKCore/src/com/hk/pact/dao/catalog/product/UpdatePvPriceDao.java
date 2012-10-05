package com.hk.pact.dao.catalog.product;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.catalog.product.ProductVariant;

public interface UpdatePvPriceDao {

	Page getPVForPriceUpdate(Category primaryCategory, boolean updated, int pageNo, int perPage);

	UpdatePvPrice getPVForPriceUpdate(ProductVariant productVariant, boolean updated);

	boolean isBrandAudited(String brand);
}