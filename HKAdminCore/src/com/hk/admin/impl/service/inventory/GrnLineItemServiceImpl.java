package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 10/29/12
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class GrnLineItemServiceImpl implements GrnLineItemService {

	public Long getPoLineItemQty(GrnLineItem grnLineItem) {
		List<PoLineItem> poLineItemList = grnLineItem.getGoodsReceivedNote().getPurchaseOrder().getPoLineItems();
		for (PoLineItem poLineItem : poLineItemList) {
			if (grnLineItem.getSku().getId().equals(poLineItem.getSku().getId())) {
				return poLineItem.getQty();
			}
		}
		return 0L;
	}

	public Long getGrnLineItemQtyAlreadySet(GoodsReceivedNote grn, Sku sku) {
		long grnLineItemQtyAlreadySet = 0;
		List<GoodsReceivedNote> allGrnForThisPO = grn.getPurchaseOrder().getGoodsReceivedNotes();
		for (GoodsReceivedNote goodsReceivedNote : allGrnForThisPO) {
			if (!goodsReceivedNote.getId().equals(grn.getId())) {
				for (GrnLineItem grnLineItemForOtherGrn : goodsReceivedNote.getGrnLineItems()) {
					if (sku.getId().equals(grnLineItemForOtherGrn.getSku().getId())) {
						grnLineItemQtyAlreadySet += grnLineItemForOtherGrn.getQty().longValue();
					}
				}
			}
		}
		return grnLineItemQtyAlreadySet;
	}

}
