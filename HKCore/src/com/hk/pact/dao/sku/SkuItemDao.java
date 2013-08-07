package com.hk.pact.dao.sku;

import java.util.List;


import com.hk.domain.sku.*;

import com.akube.framework.dao.Page;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.dto.pos.PosProductSearchDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;
import com.hk.pact.dao.BaseDao;

public interface SkuItemDao extends BaseDao {


    public List<SkuGroup> getInStockSkuGroups(Sku sku);

    public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public SkuItem getSkuItem(SkuGroup skuGroup, SkuItemStatus skuItemStatus);

    public SkuItem getSkuItem(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus);

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId);

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusList, List<SkuItemOwner> skuItemOwners);

    public List<SkuItem> getCheckedInSkuItems(Sku sku);

    public List<SkuItem> getSkuItem(Sku sku, Long id);

    public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<SkuItemOwner> skuItemOwners, Double mrp);

    public SkuItem getSkuItemWithStatusAndOwner(SkuGroup skuGroup, SkuItemStatus skuItemStatus, SkuItemOwner skuItemOwner);

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId, SkuItemOwner skuItemOwner);

    public List<PosProductSearchDto> getCheckedInSkuItems(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId);

    public List<PosSkuGroupSearchDto> getCheckedInSkuItemsByGroup(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId);

}