package com.hk.pact.dao.sku;

import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface SkuItemDao extends BaseDao {


  public List<SkuGroup> getInStockSkuGroups(Sku sku);

  public SkuItem getSkuItemToValidateDayZeroInventory(ProductVariant productVariant, String batchNumber);

  public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty);

  public void resetInventoryByBrand(String brand);

  public void resetInventory(ProductVariant productVariant);

  public List<SkuGroup> getInStockSkuGroupsByCreateDate(Sku sku);

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);


}