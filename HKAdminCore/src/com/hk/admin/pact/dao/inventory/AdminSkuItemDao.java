package com.hk.admin.pact.dao.inventory;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface AdminSkuItemDao extends BaseDao {

    public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public List<SkuItem> getInStockSkuItemsBySku(Sku sku);

    public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList);

    public List<SkuItem> getInStockSkuItemsByQty(Sku sku, Integer qty);

    public List<SkuGroup> getInStockSkuGroups(Sku sku);



  public SkuItem getSkuItemToValidateDayZeroInventory(ProductVariant productVariant, String batchNumber);

  public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty);

  public void resetInventoryByBrand(String brand);

  public void resetInventory(ProductVariant productVariant);

  public List<SkuGroup> getInStockSkuGroupsByCreateDate(Sku sku);
  public List<SkuItem> getCheckedInSkuItems(SkuGroup skuGroup);


}
