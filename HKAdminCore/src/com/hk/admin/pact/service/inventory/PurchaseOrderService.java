package com.hk.admin.pact.service.inventory;

import com.hk.domain.inventory.po.PurchaseOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PurchaseOrderService {

	public void updatePOFillRate(PurchaseOrder purchaseOrder);
}
