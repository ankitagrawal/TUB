package com.hk.pact.dao.sku;

import java.util.List;


import com.hk.domain.sku.*;

import com.akube.framework.dao.Page;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
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
    
    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus);

    //Migrated from Inventory Manager Dao
  public Long getAvailableUnBookedInventory(List<Sku> skuList);

    public Long getNetInventory(List<Sku> skuList, List<Long> skuItemStatusIds);

    public Long getNetInventory(List<Sku> skuList, Double mrp);

    // Method to get temp Booked and Booked qty
    public Long getTempOrBookedQtyOfProductVariantInQueue(ProductVariant productVariant, List<Long> skuItemStatusId, List<Long> skuItemOwnerStatusId);

    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory);

    public List<SkuItem> getCheckedInSkuItems(Sku sku, Double mrp);

    public Double getFirstcheckedInBatchMRP(ProductVariant productVariant);

    public Long getBookedQtyOfSkuInQueue(List<Sku> skuList);

    public List<CartLineItem> getClisForInPlacedOrder(ProductVariant productVariant, Double mrp);

    public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant);

    public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp);

    public boolean sicliAlreadyExists(CartLineItem cartLineItem);

}