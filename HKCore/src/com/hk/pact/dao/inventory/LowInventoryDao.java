package com.hk.pact.dao.inventory;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.LowInventory;
import com.hk.pact.dao.BaseDao;

public interface LowInventoryDao extends BaseDao {

    public LowInventory findLowInventory(ProductVariant productVariant);

    public Page findLowInventoryList(Product product, ProductVariant productVariant, Category category, String brandName, int pageNo, int perPage);

    public Page findOutOfstockInventory(Product product, ProductVariant productVariant, Category category, String brandName, int pageNo, int perPage);

    public void deleteFromLowInventoryList(ProductVariant productVariant);

}
