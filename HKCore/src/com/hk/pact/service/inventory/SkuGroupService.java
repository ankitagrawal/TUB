package com.hk.pact.service.inventory;

import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Dec 27, 2012
 * Time: 11:40:27 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SkuGroupService {

	//SkuGroupDao method
	public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant);

	public List<SkuGroup> getAllCheckedInBatches(Sku sku);

	public SkuGroup getSkuGroup(String barcode);

	public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku);

	public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku);

	public List<SkuGroup> getInStockSkuGroupByQty(Sku sku);

	public List<SkuGroup> getSkuGroupByBarcode(String barcode, Sku sku);

	public List<SkuGroup> getInStockSkuGroupByBatch(String batch, Sku sku);



	//SkuItemDao Methods

	public List<SkuGroup> getInStockSkuGroups(Sku sku);

	public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty);

	public List<SkuItem> getInStockSkuItem(SkuGroup skuGroup);

	public List<SkuItem> getInStockSkuItemByBatch(String batchNumber, Sku sku);

	public List<SkuItem> getInStockSkuItemByBarcode(String barcode, Sku sku);

//	public List<SkuItem> getInStockSkuItem(String batchNumber, Sku sku, String barcode);


}
