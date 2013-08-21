package com.hk.admin.impl.service.inventory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.inventory.StockTransferDao;
import com.hk.admin.pact.service.inventory.StockTransferService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

/**
 * @author Ankit Chhabra
 * 
 */
@Service
public class StockTransferServiceImpl implements StockTransferService {

	private static Logger logger = LoggerFactory.getLogger(StockTransferService.class);

	@Autowired
	StockTransferDao stockTransferDao;
	@Autowired
	UserService userService;
	@Autowired
	SkuItemLineItemDao skuItemLineItemDao;
	@Autowired
	SkuItemDao skuItemDao;
	@Autowired
	BaseDao baseDao;
	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	AdminOrderService adminOrderService;

	@Override
	public synchronized StockTransferLineItem updateStockTransferLineItem(StockTransferLineItem stockTransferLineItem, String actionType) {

		stockTransferDao.refresh(stockTransferLineItem);

		if (("add").equalsIgnoreCase(actionType)) {
			if (stockTransferLineItem.getCheckedoutQty() != null) {
				stockTransferLineItem.setCheckedoutQty(stockTransferLineItem.getCheckedoutQty() + 1);
			} else {
				stockTransferLineItem.setCheckedoutQty(1l);
			}
		} else if (("revert").equalsIgnoreCase(actionType)) {
			stockTransferLineItem.setCheckedoutQty(stockTransferLineItem.getCheckedoutQty() - 1);
		}
		return (StockTransferLineItem) stockTransferDao.save(stockTransferLineItem);

	}

	@Override
	public boolean validateSkuItem(SkuItem skuItem) {
		User loggedOnUser = userService.getLoggedInUser();
		SkuItemCLI skuItemCLI = skuItemLineItemDao.getSkuItemCLI(skuItem);
		SkuItemLineItem skuItemLineItem = skuItemLineItemDao.getSkuItemLineItem(skuItem);

		List<Sku> skuList = new ArrayList<Sku>();
		List<Long> skuStatusIdList = new ArrayList<Long>();
		List<Long> skuItemOwnerList = new ArrayList<Long>();

		skuStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());

		skuItemOwnerList.add(EnumSkuItemOwner.SELF.getId());
		if (skuItemLineItem != null) {
			LineItem item = skuItemLineItem.getLineItem();
			skuList.add(item.getSku());
			List<SkuItem> availableUnbookedSkuItems = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, item.getMarkedPrice());
			SkuItemCLI cli = skuItemLineItem.getSkuItemCLI();
			if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() > 0 && !availableUnbookedSkuItems.get(0).equals(skuItem)) {
				SkuItem skuItemToBeSet = availableUnbookedSkuItems.get(0);
				// if got any siblings - set it on the entries in the booking
				// table
				skuItemToBeSet.setSkuItemStatus(skuItemLineItem.getSkuItem().getSkuItemStatus());
				cli.setSkuItem(skuItemToBeSet);
				cli = (SkuItemCLI) baseDao.save(cli);
				skuItemLineItem.setSkuItem(skuItemToBeSet);
				skuItemLineItem.setSkuItemCLI(cli);
				skuItemLineItem = (SkuItemLineItem) baseDao.save(skuItemLineItem);

				shippingOrderService.logShippingOrderActivity(item.getShippingOrder(), EnumShippingOrderLifecycleActivity.SO_ITEM_RV_SUBTRACT, null,
						"The already booked unit has been freed for Stock Transfer and a new unit has been booked for variant:- "
								+ item.getSku().getProductVariant());
				logger.debug("Stock Transfer:- got a sibling - set it on the entries in the booking table against SkuItem:- " + skuItem.getId());
				return true;
			}
			return false;
		}

		if (skuItemLineItem == null && skuItemCLI != null) {
			CartLineItem cartLineItem = skuItemCLI.getCartLineItem();
			skuList.add(skuItemCLI.getSkuItem().getSkuGroup().getSku());
			List<SkuItem> availableUnbookedSkuItems = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, cartLineItem.getMarkedPrice());
			if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() > 0) {
				SkuItem skuItemToBeSet = availableUnbookedSkuItems.get(0);
				skuItemToBeSet.setSkuItemStatus(skuItemCLI.getSkuItem().getSkuItemStatus());
				skuItemCLI.setSkuItem(skuItemToBeSet);
				skuItemCLI = (SkuItemCLI) baseDao.save(skuItemCLI);
				adminOrderService.logOrderActivity(
						cartLineItem.getOrder(),
						loggedOnUser,
						EnumOrderLifecycleActivity.LoggedComment.asOrderLifecycleActivity(),
						"The already booked unit has been freed for RV subtract and a new unit has been booked for variant:- "
								+ cartLineItem.getProductVariant());
				return true;
			}
			return false;
		}
		return true;
	}

}
