package com.hk.pact.dao.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.*;
import com.hk.dto.pos.PosProductSearchDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface SkuItemDao extends BaseDao {


  public List<SkuGroup> getInStockSkuGroups(Sku sku);

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

  public SkuItem getSkuItem(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus);

  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId);

  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusList, List<SkuItemOwner> skuItemOwners);

  @Deprecated
  public List<SkuItem> getCheckedInSkuItems(Sku sku);

  public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> skuItemStatusIds, List<SkuItemOwner> skuItemOwners, Double mrp);

  public List<PosProductSearchDto> getCheckedInSkuItems(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId);

  public List<PosSkuGroupSearchDto> getCheckedInSkuItemsByGroup(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId);

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus);

  public Long getInventoryCount(List<Sku> skuList, List<Long> skuItemStatusIds);

  @Deprecated
  public List<SkuItem> getCheckedInSkuItems(Sku sku, Double mrp);

  public Long getBookedQtyOfSkuInQueue(List<Sku> skuList);

  public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant);

}