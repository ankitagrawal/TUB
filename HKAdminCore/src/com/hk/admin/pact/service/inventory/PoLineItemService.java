package com.hk.admin.pact.service.inventory;

import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.sku.Sku;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PoLineItemService {

	public void updatePoLineItemFillRate(GoodsReceivedNote grn, GrnLineItem grnLineItem, Long grnLineItemQty);

  public PoLineItem save(PoLineItem poLineItem);
}
