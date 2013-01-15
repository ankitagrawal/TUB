package com.hk.pact.dao.sku;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface SkuGroupDao extends BaseDao {

  public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant);

  public List<SkuGroup> getAllCheckedInBatches(Sku sku);
//
//  public void resetInventoryByBrand(String brand);
//
//  public void resetInventory(ProductVariant productVariant);

  public SkuGroup getSkuGroup(String barcode);

  public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku);

  public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku);

	public List<SkuGroup> getInStockSkuGroupByQty(Sku sku);

	public List<SkuGroup> getSkuGroupsByBatch(String batch, Sku sku);

	public List<SkuGroup> getSkuGroup(String barcode, Warehouse warehouse);




}
