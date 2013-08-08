package com.hk.admin.pact.dao.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemOwner;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface AdminSkuItemDao extends BaseDao {

    public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public List<SkuItem> getInStockSkuItemsBySku(Sku sku);

    public List<SkuItem> getInStockSkuItemsByQty(Sku sku, Integer qty);

    public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList);

    public List<SkuGroup> getInStockSkuGroups(Sku sku);

    public List<SkuItem> getInStockSkuItems(List<SkuGroup> skuGroupList);

    public List<SkuGroup> getInStockSkuGroupsForReview(LineItem lineItem);

     public List<SkuGroup> getSkuGroupsInReviewState();

    public SkuItem getSkuItemToValidateDayZeroInventory(ProductVariant productVariant, String batchNumber);

    public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty);

    public void resetInventoryByBrand(String brand);

    public void resetInventory(ProductVariant productVariant);

    public List<SkuGroup> getInStockSkuGroupsByCreateDate(Sku sku);

    //This seems to be wrong hence deprecating it, please use it at your own risk
    @Deprecated
    public List<SkuItem> getCheckedInSkuItems(SkuGroup skuGroup);

    public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse);
    
    public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse, List<SkuItemStatus> itemStatus, List<SkuItemOwner> itemOwners);


}
