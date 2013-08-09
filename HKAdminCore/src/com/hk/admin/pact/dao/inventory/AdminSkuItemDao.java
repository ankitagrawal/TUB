package com.hk.admin.pact.dao.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface AdminSkuItemDao extends BaseDao {
    // todo --
    public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public List<SkuItem> getNetPhysicalAvailableStockSkuItems(SkuGroup skuGroup);

    public List<SkuItem> getInStockSkuItemsBySku(Sku sku);
    // todo -- deleted
    public List<SkuItem> getInStockSkuItemsByQty(Sku sku, Integer qty);

    public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList);

    public List<SkuGroup> getInStockSkuGroups(Sku sku);

    public List<SkuItem> getInStockSkuItems(List<SkuGroup> skuGroupList);

    public List<SkuGroup> getInStockSkuGroupsForReview(LineItem lineItem);

     public List<SkuGroup> getSkuGroupsInReviewState();
      // todo -- deleted
    public SkuItem getSkuItemToValidateDayZeroInventory(ProductVariant productVariant, String batchNumber);
     // todo -- deleted
    public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty);

   // todo -- deleted
    public void resetInventoryByBrand(String brand);
       // todo -- deleted
    public void resetInventory(ProductVariant productVariant);

  // todo -- deleted
    public List<SkuGroup> getInStockSkuGroupsByCreateDate(Sku sku);

    //This seems to be wrong hence deprecating it, please use it at your own risk
  // todo -- deleted
    @Deprecated
    public List<SkuItem> getCheckedInSkuItems(SkuGroup skuGroup);

    public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse);


}
