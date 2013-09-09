package com.hk.admin.pact.service.inventory;

import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.sku.SkuItem;

/**
 * @author Ankit Chhabra
 *
 */
public interface StockTransferService {

	public StockTransferLineItem updateStockTransferLineItem(StockTransferLineItem stockTransferLineItem, String actionType);
	
	public boolean validateSkuItem(SkuItem skuItem);

}

