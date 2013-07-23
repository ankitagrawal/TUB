package com.hk.impl.dao.inventoryManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.constants.sku.EnumSkuItemStatus;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: Ankit Date: Jul 23, 2013 Time: 4:06:36 PM To
 * change this template use File | Settings | File Templates.
 */

@Service
public class InventoryManageServiceImpl {

	@Autowired
	SkuService skuService;
	@Autowired
	SkuGroupService skuGroupService;
	@Autowired
	BaseDao baseDao;

	public void bookSkuLineItemForOrder(Order order) {
		Set<CartLineItem> cartLineItems = order.getCartLineItems();
		for (CartLineItem cartLineItem : cartLineItems) {
			cartLineItem.getProductVariant();
			List<Sku> skus = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());
			List<SkuItem> skuItems = getSkuItems(skus, cartLineItem.getProductVariant().getMarkedPrice(), EnumSkuItemStatus.Checked_IN);
			long qtyToBeSet = cartLineItem.getQty();

			for (int i = 0; i < qtyToBeSet; i++) {
				for (SkuItem si : skuItems) {
					si.setSkuItemStatus(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
					si.setSkuItemOwnerStatus(null);
				}
			}

			// get skuItem corresponfing to this variant

		}

	}

	public List<SkuItem> getSkuItems(List<Sku> skus, Double mrp, EnumSkuItemStatus status) {
		List<SkuItem> skuItemList = new ArrayList<SkuItem>();
		if (skus != null && skus.size() > 0) {
			for (Sku sku : skus) {
				List<SkuItem> skuItems = skuGroupService.getSkuItem(sku, status.getId());
				if (skuItems != null && skuItems.size() > 0) {
					for (SkuItem item : skuItems) {
						if (item.getSkuGroup().getMrp().equals(mrp)) {
							skuItemList.add(item);
						}
					}
				}
			}
		}
		return skuItemList;
	}

	public void checkoutMethod(LineItem lineItem, SkuItem skuItem, Long checkoutNum) {
		List<SkuItemLineItem> skuItemLineItems = getSkuItemLineItem(lineItem);
		List<SkuItem> skuItemInSkuItemLineItems = new ArrayList<SkuItem>();
		for(SkuItemLineItem sili : skuItemLineItems){
			SkuItem si = sili.getSkuItem();
			skuItemInSkuItemLineItems.add(si);
		}
		if (skuItemInSkuItemLineItems.contains(skuItem)) {
			// TODO:

		} else {
			//If skuItem
			if (skuItem.getSkuItemStatus().equals(EnumSkuItemStatus.BOOKED.getSkuItemStatus())) {
				SkuItemLineItem item = new SkuItemLineItem();
				item.setSkuItem(skuItem);
				item.setProductVariant(lineItem.getSku().getProductVariant());
				item.setWaitNumber(checkoutNum);
				item.setLineItem(lineItem);
				baseDao.save(item);
				
				//release the already present
				SkuItem toReleaseSkuItem = skuItemInSkuItemLineItems.get(0);
				toReleaseSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
				baseDao.save(toReleaseSkuItem);
				//delete the entry from table t1
				baseDao.delete(toReleaseSkuItem);
			}
			if (skuItem.getSkuItemStatus().equals(EnumSkuItemStatus.Checked_IN.getSkuItemStatus())) {
				// flip skuItem with a booked SI.
				// 1. Find a booked SI for this one

				//book the CSI
				skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
				skuItem = (SkuItem) baseDao.save(skuItem);
				
				SkuItemLineItem item = new SkuItemLineItem();
				item.setSkuItem(skuItem);
				item.setProductVariant(lineItem.getSku().getProductVariant());
				item.setWaitNumber(checkoutNum);
				item.setLineItem(lineItem);
				baseDao.save(item);
				
				//release the already present 
				SkuItem toReleaseSkuItem = skuItemInSkuItemLineItems.get(0);
				toReleaseSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
				baseDao.save(toReleaseSkuItem);
				//delete the entry from table t1
				baseDao.delete(toReleaseSkuItem);
				
			}
		}
	}

	// search for SkuItemCLI
	public List<SkuItemLineItem> getSkuItemLineItem(LineItem item) {
		// TODO : getSkuItemLineItem
		List<SkuItemLineItem> skuItemLineItems = null;
		return skuItemLineItems;
	}

}
