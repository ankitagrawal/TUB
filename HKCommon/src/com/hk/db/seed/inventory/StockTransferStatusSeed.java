package com.hk.db.seed.inventory;

import com.hk.constants.inventory.EnumStockTransferStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.StockTransferStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/18/13
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class StockTransferStatusSeed extends BaseSeedData {
	public void insert(java.lang.String name, java.lang.Long id) {
		StockTransferStatus stockTransferStatus = new StockTransferStatus();
		stockTransferStatus.setName(name);
		stockTransferStatus.setId(id);
		save(stockTransferStatus);
	}

	public void invokeInsert() {
		List<Long> pkList = new ArrayList<Long>();

		for (EnumStockTransferStatus enumStockTransferStatus : EnumStockTransferStatus.values()) {

			if (pkList.contains(enumStockTransferStatus.getId()))
				throw new RuntimeException("Duplicate key " + enumStockTransferStatus.getId());
			else
				pkList.add(enumStockTransferStatus.getId());

			insert(enumStockTransferStatus.getName(), enumStockTransferStatus.getId());
		}
	}

}
