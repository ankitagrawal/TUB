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
	public synchronized StockTransferLineItem updateStockTransferLineItem(Long itemId, String actionType) {
		
		StockTransferLineItem lineItem = stockTransferDao.get(StockTransferLineItem.class, itemId);
		
		if(("add").equalsIgnoreCase(actionType)) {
			if (lineItem.getCheckedoutQty()!=null) {
				lineItem.setCheckedoutQty(lineItem.getCheckedoutQty() + 1);
			} else {
				lineItem.setCheckedoutQty(1l);
			}
		} else if (("revert").equalsIgnoreCase(actionType)) {
			lineItem.setCheckedoutQty(lineItem.getCheckedoutQty() - 1);
		}
		return (StockTransferLineItem) stockTransferDao.save(lineItem);

	}

}
