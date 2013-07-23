package com.hk.pact.service.inventory;

import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;


import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 27, 2012
 * Time: 11:40:27 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SkuGroupService {

    //SkuGroupDao method
    public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant);

    public List<SkuGroup> getAllCheckedInBatches(Sku sku);

    public SkuGroup getInStockSkuGroup(String barcode, Long warehouseId);

    public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku);

    public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku);

    public List<SkuGroup> getInStockSkuGroupByQty(Sku sku);

    public List<SkuGroup> getSkuGroupsByBatch(String batch, Sku sku);

    public List<SkuGroup> getSkuGroup(String barcode, Long warehouseId);

    public List<SkuGroup> getSkuGroupByGrnLineItem(GrnLineItem grnLineItem);

    public void deleteSkuGroup(SkuGroup skuGroup);

    public List<SkuGroup> getAllInStockSkuGroups(Sku sku);
    
    public List<SkuGroup> getAllCheckedInBatchForGrn(GoodsReceivedNote grn);


    //SkuItemDao Methods

    public List<SkuGroup> getInStockSkuGroups(Sku sku);

    public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty);

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

    public SkuItem getSkuItem(SkuGroup skuGroup, SkuItemStatus skuItemStatus);

    public List<SkuGroup> getSkuGroupsByBarcodeForStockTransfer(String barcode, Long warehouseId);

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId);

    public SkuItem saveSkuItem(SkuItem skuItem);

    public void deleteAllSkuItemsOfSkuGroup(SkuGroup skuGroup);

    public List<SkuItem> getCheckedInSkuItems(Sku sku);
    
    public List<SkuItem> getSkuItem(Sku sku, Long id);


}
