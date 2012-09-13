package com.hk.pact.dao.catalog.product;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;

public interface UpdatePvPriceDao {

	Page getPVForPriceUpdate(Category primaryCategory, boolean updated, int pageNo, int perPage);
}