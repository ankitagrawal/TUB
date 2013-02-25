package com.hk.impl.service.inventory;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;


import java.util.List;

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

    public List<SkuGroup> getCheckedInSkuGroup(String brand, Warehouse warehouse, Product product){
        return skuGroupDao.getCheckedInSkuGroup(brand , warehouse, product);
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


}
