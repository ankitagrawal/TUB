package com.hk.pact.dao.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface SkuGroupDao extends BaseDao {

  public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant);

  public List<SkuGroup> getAllCheckedInBatches(Sku sku);

  public SkuGroup getInStockSkuGroup(String barcode, Long warehouseId);

  public SkuGroup getInStockSkuGroup(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusIds);

  public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku);

  public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku);

  public List<SkuGroup> getSkuGroup(String barcode, Long warehouseId);

  public List<SkuGroup> getSkuGroupsByBarcodeForStockTransfer(String barcode, Long warehouseId);

  public List<SkuGroup> getAllInStockSkuGroups(Sku sku);

  public List<SkuGroup> getSkuGroupByGrnLineItem(GrnLineItem grnLineItem);

  public List<SkuGroup> getAllCheckedInBatchForGrn(GoodsReceivedNote grn);

}
