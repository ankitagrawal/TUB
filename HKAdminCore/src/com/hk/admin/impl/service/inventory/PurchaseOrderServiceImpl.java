package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	PurchaseOrderDao purchaseOrderDao;

	public void updatePOFillRate(PurchaseOrder purchaseOrder) {
		long totalAskedQty = 0;
		long totalReceivedQty = 0;
		double fillRate = 0.0;

		for(PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {
			totalAskedQty += poLineItem.getQty();
			if(poLineItem.getReceivedQty() != null) {
				totalReceivedQty += poLineItem.getReceivedQty();
			}
		}

		if(totalAskedQty > 0) {
			fillRate = (totalReceivedQty * 100.0)/ totalAskedQty;
		}
		purchaseOrder.setFillRate(fillRate);
		getPurchaseOrderDao().saveOrUpdate(purchaseOrder);

	}

	public PurchaseOrderDao getPurchaseOrderDao() {
		return purchaseOrderDao;
	}

	public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
		this.purchaseOrderDao = purchaseOrderDao;
	}
}
