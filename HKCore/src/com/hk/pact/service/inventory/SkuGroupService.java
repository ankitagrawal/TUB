package com.hk.pact.service.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 27, 2012
 * Time: 11:40:27 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SkuGroupService {

  /**
   * SkuGroupDao Methods *
   */
  public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant);

  public List<SkuGroup> getAllCheckedInBatches(Sku sku);

  public SkuGroup getInStockSkuGroup(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusIds);

  public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku);

  public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku);

  public List<SkuGroup> getSkuGroup(String barcode, Long warehouseId);

  public List<SkuGroup> getSkuGroupByGrnLineItem(GrnLineItem grnLineItem);

  public void deleteSkuGroup(SkuGroup skuGroup);

  public List<SkuGroup> getAllInStockSkuGroups(Sku sku);

  public List<SkuGroup> getAllCheckedInBatchForGrn(GoodsReceivedNote grn);

  /**
   * SkuItemDao Methods *
   */
  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus);

  public SkuItem getSkuItem(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatusList);

  public List<SkuGroup> getSkuGroupsByBarcodeForStockTransfer(String barcode, Long warehouseId);

  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId);

  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusList, List<SkuItemOwner> skuItemOwners);

  public SkuItem saveSkuItem(SkuItem skuItem);

  public void deleteAllSkuItemsOfSkuGroup(SkuGroup skuGroup);

  public List<SkuItem> getCheckedInSkuItems(Sku sku);

  /**
   * A generic method for fetching List of SkuItems with given Sku, status and SkuItemOwners
   *
   * @param skuList
   * @param statusIds
   * @param skuItemOwners
   * @return
   */
  public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<Long> skuItemOwners, Double mrp);

}