package com.hk.impl.service.inventory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryHealthService.FetchType;
import com.hk.pact.service.inventory.InventoryHealthService.SkuFilter;
import com.hk.pact.service.inventory.OrderReviewService;

public class OrderReviewServiceImpl implements OrderReviewService {

	@Autowired InventoryHealthService inventoryHealthService;
	@Autowired ProductVariantService productVariantService;
	
	@Override
	public void fixLineItem(LineItem lineItem) throws CouldNotFixException {
		Order order = lineItem.getCartLineItem().getOrder();
		
		ProductVariant variant = productVariantService.getVariantById(lineItem.getCartLineItem().getProductVariant().getId());
		SkuFilter filter = new SkuFilter();
		filter.setFetchType(FetchType.ALL);
		filter.setMinQty(lineItem.getQty());
		filter.setMrp(variant.getMarkedPrice());
		filter.setWarehouseId(lineItem.getSku().getWarehouse().getId());
		
		Collection<Sku> skus = inventoryHealthService.getAvailableSkus(variant, filter);
		for (Sku sku : skus) {
			
		}
	}
}
