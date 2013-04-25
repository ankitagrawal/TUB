package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PoLineItemServiceImpl implements PoLineItemService {

	@Autowired
	GrnLineItemService grnLineItemService;
	@Autowired
	PoLineItemDao poLineItemDao;

	public void updatePoLineItemFillRate(GoodsReceivedNote grn, GrnLineItem grnLineItem, Long grnLineItemQty) {

		Long totalLineItemQtyInOtherGrns = getGrnLineItemService().getGrnLineItemCheckedInQty(grn, grnLineItem.getSku());
		Long poLineItemQty = getGrnLineItemService().getPoLineItemQty(grnLineItem);
		Double fillRate = 0D;

		PoLineItem poLineItem = getPoLineItemDao().getPoLineItem(grn.getPurchaseOrder(), grnLineItem.getSku().getProductVariant());
		if (poLineItem != null) {
			if (grnLineItemQty != null && (totalLineItemQtyInOtherGrns + grnLineItemQty) > 0) {
				Long totalReceivedQty = totalLineItemQtyInOtherGrns + grnLineItemQty;
				if(poLineItemQty > 0) {
					fillRate = (100.0 * totalReceivedQty) / poLineItemQty;
				}
				poLineItem.setReceivedQty(totalReceivedQty);
			}
			poLineItem.setFillRate(fillRate);
			getPoLineItemDao().save(poLineItem);
		}
	}

  public PoLineItem save(PoLineItem poLineItem){
    return (PoLineItem)getPoLineItemDao().save(poLineItem);
  }

	public GrnLineItemService getGrnLineItemService() {
		return grnLineItemService;
	}

	public void setGrnLineItemService(GrnLineItemService grnLineItemService) {
		this.grnLineItemService = grnLineItemService;
	}

	public PoLineItemDao getPoLineItemDao() {
		return poLineItemDao;
	}

	public void setPoLineItemDao(PoLineItemDao poLineItemDao) {
		this.poLineItemDao = poLineItemDao;
	}
}
