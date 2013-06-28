package com.hk.impl.service.inventory;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
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
import com.hk.pact.service.shippingOrder.ShippingOrderService;

@Service
public class OrderReviewServiceImpl implements OrderReviewService {

	@Autowired InventoryHealthService inventoryHealthService;
	@Autowired ProductVariantService productVariantService;
	@Autowired LineItemDao lineItemDao;
	@Autowired CartLineItemService cartLineItemService;
	
	@Autowired EmailManager emailManager;
	@Autowired UserService userService;
	@Autowired FixedShippingOrderDao fixedShippingOrderDao;
	@Autowired ShippingOrderService shippingOrderService;
	
	@Override
	@Transactional
	public void fixLineItem(LineItem lineItem) {
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
			fail(lineItem, "No Sku Item is available with different MRP. Escalate back to action queue.");
			return;
		}
		
		if(selectedInfo.getMrp() > lineItem.getMarkedPrice().doubleValue()) {
			double existingMrp = lineItem.getMarkedPrice().doubleValue();
			updateHighMrp(lineItem, selectedInfo);
			success(lineItem, existingMrp, lineItem.getHkPrice());
		} else {
			fail(lineItem, "No Sku Item is available with Higher MRP. Escalate back to action queue.");
			return;
		}
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
	
	private void success(LineItem lineItem, double previousMrp, double preHkPrice) {
		FixedShippingOrder fso = new FixedShippingOrder();
		fso.setCreateDate(new Date());
		fso.setUpdateDate(new Date());
		fso.setShippingOrder(lineItem.getShippingOrder());
		fso.setCreatedBy(userService.getLoggedInUser());
		fso.setStatus("OPEN");

		StringBuilder remarks = new StringBuilder("Line Item: " + lineItem.getId());  
		remarks.append("<br/> Previous MRP: " + previousMrp);
		remarks.append("<br/> New MRP: " + lineItem.getMarkedPrice());
		remarks.append("<br/> Previous HK Price: " + preHkPrice);
		remarks.append("<br/> New HK Price: " + lineItem.getHkPrice());
		
		fso.setRemarks(remarks.toString());
		fixedShippingOrderDao.save(fso);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("variantName", lineItem.getCartLineItem().getProductVariant().getId());
		map.put("gatewayId", lineItem.getShippingOrder().getGatewayOrderId());
		map.put("quantity", String.valueOf(lineItem.getQty()));
		map.put("previousMrp", String.valueOf(previousMrp));
		map.put("newMrp", String.valueOf(lineItem.getMarkedPrice()));
		map.put("prevHKPrice", String.valueOf(preHkPrice));
		map.put("newHKPrice", String.valueOf(lineItem.getHkPrice()));
		
		emailManager.sendSoFixedMail(map);

		shippingOrderService.logShippingOrderActivity(lineItem.getShippingOrder(), 
				EnumShippingOrderLifecycleActivity.SO_LineItemFixed, null, remarks.toString());
	}
	
	private void fail(LineItem lineItem, String reason) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("variantName", lineItem.getCartLineItem().getProductVariant().getId());
		map.put("gatewayId", lineItem.getShippingOrder().getGatewayOrderId());
		map.put("quantity", String.valueOf(lineItem.getQty()));
		map.put("mrp", String.valueOf(lineItem.getMarkedPrice()));
		map.put("reason", reason);
		
		emailManager.sendSoFixFailedMail(map);
		
		shippingOrderService.logShippingOrderActivity(lineItem.getShippingOrder(), 
				EnumShippingOrderLifecycleActivity.SO_LineItemCouldNotFixed, null, reason);
	}
}
