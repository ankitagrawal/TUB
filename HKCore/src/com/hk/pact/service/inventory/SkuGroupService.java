package com.hk.pact.service.inventory;

import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
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

	public SkuGroup getSkuGroup(String barcode);

	public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku);

	public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku);

	public List<SkuGroup> getInStockSkuGroupByQty(Sku sku);




	//SkuItemDao Methods

	public List<SkuGroup> getInStockSkuGroups(Sku sku);

	public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty);

}
