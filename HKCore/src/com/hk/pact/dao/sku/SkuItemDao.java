package com.hk.pact.dao.sku;

import java.util.List;

import com.hk.domain.sku.*;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;

public interface SkuItemDao extends BaseDao {


    public List<SkuGroup> getInStockSkuGroups(Sku sku);

    public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public SkuItem getSkuItem(SkuGroup skuGroup, SkuItemStatus skuItemStatus);

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId);

    public List<SkuItem> getCheckedInSkuItems(Sku sku);
    
    public List<SkuItem> getSkuItem(Sku sku, Long id);

    public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<SkuItemOwner> skuItemOwners, Double mrp);

    public SkuItem getSkuItemWithStatusAndOwner(SkuGroup skuGroup, SkuItemStatus skuItemStatus, SkuItemOwner skuItemOwner);

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId, SkuItemOwner skuItemOwner);


}