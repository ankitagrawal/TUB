package com.hk.admin.pact.service.inventory;

import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 10/29/12
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GrnLineItemService {

	public Long getPoLineItemQty(GrnLineItem grnLineItem);

	public Long getGrnLineItemQtyAlreadySet(GoodsReceivedNote grn ,Sku sku);

    public boolean isAllSkuItemInCheckedInStatus(GrnLineItem grnLineItem);

    public void delete(GrnLineItem grnLineItem);
    
    public Long getGrnLineItemCheckedInQty(GoodsReceivedNote grn ,Sku sku);

}
