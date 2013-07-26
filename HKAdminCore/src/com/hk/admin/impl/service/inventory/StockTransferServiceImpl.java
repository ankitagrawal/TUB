package com.hk.admin.impl.service.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.inventory.StockTransferDao;
import com.hk.admin.pact.service.inventory.StockTransferService;
import com.hk.domain.inventory.StockTransferLineItem;

/**
 * @author Ankit Chhabra
 *
 */
@Service
public class StockTransferServiceImpl implements StockTransferService {

	@Autowired
	StockTransferDao stockTransferDao;

	@Override
	public synchronized StockTransferLineItem updateStockTransferLineItem(StockTransferLineItem stockTransferLineItem, String actionType) {
		
		stockTransferDao.refresh(stockTransferLineItem);
		
		if(("add").equalsIgnoreCase(actionType)) {
			if (stockTransferLineItem.getCheckedoutQty()!=null) {
				stockTransferLineItem.setCheckedoutQty(stockTransferLineItem.getCheckedoutQty() + 1);
			} else {
				stockTransferLineItem.setCheckedoutQty(1l);
			}
		} else if (("revert").equalsIgnoreCase(actionType)) {
			stockTransferLineItem.setCheckedoutQty(stockTransferLineItem.getCheckedoutQty() - 1);
		}
		return (StockTransferLineItem) stockTransferDao.save(stockTransferLineItem);

	}

}
