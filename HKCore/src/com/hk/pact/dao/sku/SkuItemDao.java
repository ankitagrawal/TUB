package com.hk.pact.dao.sku;

import java.util.List;

import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;

public interface SkuItemDao extends BaseDao {


    public List<SkuGroup> getInStockSkuGroups(Sku sku);

    public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public SkuItem getSkuItem(SkuGroup skuGroup, SkuItemStatus skuItemStatus);

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId);

    public List<SkuItem> getCheckedInSkuItems(Sku sku);

}