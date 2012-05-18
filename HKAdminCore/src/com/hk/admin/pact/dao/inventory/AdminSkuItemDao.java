package com.hk.admin.pact.dao.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface AdminSkuItemDao extends BaseDao {

    public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public List<SkuItem> getInStockSkuItemsBySku(Sku sku);

    public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList);

    public List<SkuItem> getInStockSkuItemsByQty(Sku sku, Integer qty);

    public List<SkuGroup> getInStockSkuGroups(Sku sku); 

}
