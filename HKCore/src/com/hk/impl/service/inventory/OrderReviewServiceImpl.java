package com.hk.impl.service.inventory;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.FixedShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.shippingOrder.FixedShippingOrderDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryHealthService.FetchType;
import com.hk.pact.service.inventory.InventoryHealthService.SkuFilter;
import com.hk.pact.service.inventory.InventoryHealthService.SkuInfo;
import com.hk.pact.service.inventory.OrderReviewService;
import com.hk.pact.service.order.CartLineItemService;

@Service
public class OrderReviewServiceImpl implements OrderReviewService {

	@Autowired InventoryHealthService inventoryHealthService;
	@Autowired ProductVariantService productVariantService;
	@Autowired LineItemDao lineItemDao;
	@Autowired CartLineItemService cartLineItemService;
	
	@Autowired EmailManager emailManager;
	@Autowired UserService userService;
	@Autowired FixedShippingOrderDao fixedShippingOrderDao;
	
	@Override
	@Transactional
	public void fixLineItem(LineItem lineItem) throws CouldNotFixException {
		ProductVariant variant = productVariantService.getVariantById(lineItem.getCartLineItem().getProductVariant().getId());
		
		SkuFilter filter = new SkuFilter();
		filter.setFetchType(FetchType.ALL);
		filter.setMinQty(lineItem.getQty());
		filter.setWarehouseId(lineItem.getSku().getWarehouse().getId());
		
		Collection<SkuInfo> skuInfos = inventoryHealthService.getAvailableSkus(variant, filter);
		
		double bestDelta = Double.MAX_VALUE;
		SkuInfo selectedInfo = null;
		
		if(skuInfos != null) {
			for (SkuInfo skuInfo : skuInfos) {
				double delta =  Math.abs(skuInfo.getMrp() - lineItem.getMarkedPrice().doubleValue());
				if(delta == 0d) continue;
				if(delta < bestDelta) {
					bestDelta = delta;
					selectedInfo = skuInfo;
				}
			}
		}
		
		if(selectedInfo == null) {
			throw new CouldNotFixException("Failed to fix the SO. Escalate back the SO.");
		}
		
		double existingMrp = lineItem.getMarkedPrice().doubleValue();
		double existingHkPrice = lineItem.getHkPrice().doubleValue();
		if(selectedInfo.getMrp() > lineItem.getMarkedPrice().doubleValue()) {
			updateHighMrp(lineItem, selectedInfo);
		} else {
			updateLowMrp(lineItem, selectedInfo);
		}
		
		recordAndMail(lineItem, existingMrp, existingHkPrice);
		inventoryHealthService.checkInventoryHealth(variant);
	}
	
	private void updateHighMrp(LineItem lineItem, SkuInfo skuInfo) {
		lineItem = lineItemDao.get(LineItem.class, lineItem.getId());
		lineItem.setMarkedPrice(skuInfo.getMrp());
		lineItemDao.save(lineItem);
		
		CartLineItem cartLineItem = lineItem.getCartLineItem();
		cartLineItem.setMarkedPrice(skuInfo.getMrp());
		cartLineItemService.save(cartLineItem);
	}
	
	private void updateLowMrp(LineItem lineItem, SkuInfo skuInfo) {
		double discount = 1d - lineItem.getHkPrice()/lineItem.getMarkedPrice();
		double newHkPrice = skuInfo.getMrp() * (1d - discount);
		
		lineItem = lineItemDao.get(LineItem.class, lineItem.getId());
		lineItem.setMarkedPrice(skuInfo.getMrp());
		lineItem.setHkPrice(newHkPrice);
		lineItemDao.save(lineItem);
		
		CartLineItem cartLineItem = lineItem.getCartLineItem();
		cartLineItem.setMarkedPrice(skuInfo.getMrp());
		cartLineItem.setHkPrice(newHkPrice);
		cartLineItemService.save(cartLineItem);
	}
	
	private void recordAndMail(LineItem lineItem, double previousMrp, double preHkPrice) {
		FixedShippingOrder fso = new FixedShippingOrder();
		fso.setCreateDate(new Date());
		fso.setUpdateDate(new Date());
		fso.setShippingOrder(lineItem.getShippingOrder());
		fso.setCreatedBy(userService.getLoggedInUser());
		fso.setStatus("OPEN");

		StringBuilder remarks = new StringBuilder("Line Item: " + lineItem.getId());  
		remarks.append("\n Previous MRP: " + previousMrp);
		remarks.append("\n New MRP: " + lineItem.getMarkedPrice());
		remarks.append("\n Previous HK Price: " + preHkPrice);
		remarks.append("\n New HK Price: " + lineItem.getHkPrice());
		
		fso.setRemarks(remarks.toString());
		fixedShippingOrderDao.save(fso);
	}
}
