package com.hk.impl.service.inventory;

import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.*;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 27, 2012
 * Time: 11:43:25 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SkuGroupServiceImpl implements SkuGroupService {

    @Autowired
    SkuGroupDao skuGroupDao;
    @Autowired
    SkuItemDao skuItemDao;


    //SkuGroupDao Methods
    public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant) {
        return skuGroupDao.getAllCheckedInBatches(productVariant);
    }

    public List<SkuGroup> getAllCheckedInBatches(Sku sku) {
        return skuGroupDao.getAllCheckedInBatches(sku);
    }

    public SkuGroup getInStockSkuGroup(String barcode, Long warehouseId) {
        return skuGroupDao.getInStockSkuGroup(barcode, warehouseId);
    }

    public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku) {
        return skuGroupDao.getCurrentCheckedInBatchGrn(grn, sku);
    }

    public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku) {
        return skuGroupDao.getCurrentCheckedInBatchNotInGrn(grn, sku);
    }

    public List<SkuGroup> getInStockSkuGroupByQty(Sku sku) {
        return skuGroupDao.getInStockSkuGroupByQty(sku);
    }

    public List<SkuGroup> getSkuGroupsByBatch(String batch, Sku sku) {
        return skuGroupDao.getSkuGroupsByBatch(batch, sku);
    }

    public List<SkuGroup> getSkuGroup(String barcode, Long warehouseId) {
        return skuGroupDao.getSkuGroup(barcode, warehouseId);
    }

    public List<SkuGroup> getSkuGroupByGrnLineItem(GrnLineItem grnLineItem) {
        return skuGroupDao.getSkuGroupByGrnLineItem(grnLineItem);
    }

    @Transactional
    public void deleteSkuGroup(SkuGroup skuGroup) {
        skuGroupDao.delete(skuGroup);
    }

    public List<SkuGroup> getAllInStockSkuGroups(Sku sku) {
        return skuGroupDao.getAllInStockSkuGroups(sku);
    }


    //SkuItemDao Methods
    public List<SkuGroup> getInStockSkuGroups(Sku sku) {
        return skuItemDao.getInStockSkuGroups(sku);
    }

    public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty) {
        return skuItemDao.getMinMRPUnbookedSkuGroup(productVariant, bookedQty);
    }


    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
        return skuItemDao.getInStockSkuItems(skuGroup);
    }

    public SkuItem getSkuItem(SkuGroup skuGroup, SkuItemStatus skuItemStatus) {
        return skuItemDao.getSkuItem(skuGroup, skuItemStatus);
    }

    public List<SkuGroup> getSkuGroupsByBarcodeForStockTransfer(String barcode, Long warehouseId) {
        return skuGroupDao.getSkuGroupsByBarcodeForStockTransfer(barcode, warehouseId);
    }


    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId) {
        return skuItemDao.getSkuItemByBarcode(barcode, warehouseId, statusId);
    }

    public SkuItem saveSkuItem(SkuItem skuItem) {
        return (SkuItem) skuItemDao.save(skuItem);
    }

    @Transactional
    public void deleteAllSkuItemsOfSkuGroup(SkuGroup skuGroup) {
        Set<SkuItem> skuItemList = skuGroup.getSkuItems();
        skuGroup.setSkuItems(null);
        skuGroup = (SkuGroup) skuGroupDao.save(skuGroup);
        for (SkuItem skuItem : skuItemList) {
            skuItemDao.delete(skuItem);
        }
    }

    public List<SkuItem> getCheckedInSkuItems(Sku sku) {
        return skuItemDao.getCheckedInSkuItems(sku);
    }
    
    public List<SkuItem> getSkuItem(Sku sku, Long id){
    	return skuItemDao.getSkuItem(sku, id);
    }

    public List<SkuGroup> getAllCheckedInBatchForGrn(GoodsReceivedNote grn) {	
    	return skuGroupDao.getAllCheckedInBatchForGrn(grn);
    }

    public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<SkuItemOwner> skuItemOwners){
        return skuItemDao.getSkuItems(skuList, statusIds, skuItemOwners);
    }

    public SkuItem getSkuItemWithStatusAndOwner(SkuGroup skuGroup, SkuItemStatus skuItemStatus, SkuItemOwner skuItemOwner){
        return skuItemDao.getSkuItemWithStatusAndOwner(skuGroup, skuItemStatus, skuItemOwner);
    }

    public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId, SkuItemOwner skuItemOwner){
        return skuItemDao.getSkuItemByBarcode(barcode, warehouseId, statusId, skuItemOwner);
    }

}
