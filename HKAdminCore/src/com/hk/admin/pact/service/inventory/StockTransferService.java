package com.hk.admin.pact.service.inventory;

import com.hk.domain.inventory.StockTransferLineItem;

/**
 * @author Ankit Chhabra
 *
 */
public interface StockTransferService {

	public StockTransferLineItem updateStockTransferLineItem(Long itemId, String actionType);

}

