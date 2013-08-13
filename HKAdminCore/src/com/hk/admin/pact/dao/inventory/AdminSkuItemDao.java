package com.hk.admin.pact.dao.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface AdminSkuItemDao extends BaseDao {
  // todo --
  public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse);

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

  public List<SkuItem> getNetPhysicalAvailableStockSkuItems(SkuGroup skuGroup);

  public List<SkuItem> getCheckedInSkuItems(SkuGroup skuGroup);

  public List<SkuItem> getInStockSkuItemsBySku(Sku sku);


  public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList);

  public List<SkuGroup> getInStockSkuGroups(Sku sku);

  public List<SkuItem> getInStockSkuItems(List<SkuGroup> skuGroupList);

  public List<SkuGroup> getInStockSkuGroupsForReview(LineItem lineItem);

  public List<SkuGroup> getSkuGroupsInReviewState();

  public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse);

  public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse, List<SkuItemStatus> itemStatus, List<SkuItemOwner> itemOwners);


}
