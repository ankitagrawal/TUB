package com.hk.impl.service.inventory;

import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public SkuGroup getSkuGroup(String barcode) {
		return skuGroupDao.getSkuGroup(barcode);
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


	public List<SkuGroup> getSkuGroup(String barcode, Sku sku) {
		return skuGroupDao.getSkuGroup(barcode, sku);
	}

	public List<SkuGroup> getInStockSkuGroup(String batch, Sku sku) {
		return skuGroupDao.getInStockSkuGroup(batch, sku);
	}



	//SkuItemDao Methods
	public List<SkuGroup> getInStockSkuGroups(Sku sku) {
		return skuItemDao.getInStockSkuGroups(sku);
	}

	public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty) {
		return skuItemDao.getMinMRPUnbookedSkuGroup(productVariant, bookedQty);
	}

	public List<SkuItem> getInStockSkuItem(SkuGroup skuGroup) {
		return getInStockSkuItem(skuGroup);
	}

	public List<SkuItem> getInStockSkuItem(String batchNumber, Sku sku, String barcode) {
		List<SkuGroup> skuGroupList;
		if (barcode != null) {
			skuGroupList = skuGroupDao.getSkuGroup(barcode, sku);
		} else {
			skuGroupList = skuGroupDao.getInStockSkuGroup(batchNumber, sku);
		}
		List<SkuItem> skuItemList = new ArrayList<SkuItem>();
		for (SkuGroup skuGroup : skuGroupList) {
			skuItemList.addAll(skuGroup.getSkuItems());
		}
		return skuItemList;

	}


}
