package com.hk.splitter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.warehouse.Warehouse;

public class LineItemContainer {

	private Map<Classification, LineItemClassification> container = new LinkedHashMap<Classification, LineItemClassification>();

	public void addLineItem(Warehouse warehouse, CartLineItem cartLineItem) {
		Product product = cartLineItem.getProductVariant().getProduct();
		Classification classification = classifyProduct(product);
		String bucketId = null;
		if (classification == Classification.DROPSHIP) {
			bucketId = product.getSupplier().getTinNumber();
		}
		LineItemClassification bucket = container.get(classification);
		if (bucket == null) {
			bucket = new LineItemClassification(bucketId, classification);
		}
		bucket.addLineItem(warehouse, cartLineItem);
		container.put(classification, bucket);
	}
	
	public void testAddLineItem(Warehouse warehouse, CartLineItem cartLineItem) {
		LineItemClassification bucket = container.get(Classification.GROUNDSHIP);
		if (bucket == null) {
			bucket = new LineItemClassification(null, Classification.GROUNDSHIP);
		}
		bucket.addLineItem(warehouse, cartLineItem);
		container.put(Classification.GROUNDSHIP, bucket);
	}
	
	public static enum Classification {
		SERVICE, DROPSHIP, GROUNDSHIP, AIRSHIP, NONE, TIN
	}

	public static enum DispatchDay {
		B3, B6, B9, B12, BMAX
	}

	public Collection<LineItemClassification> getAllClassifications() {
		return container.values();
	}

	private Classification classifyProduct(Product product) {
		Classification classification = Classification.NONE;
		if (product.isService()) {
			classification = Classification.SERVICE;
		} else if (product.isDropShipping()) {
			classification = Classification.DROPSHIP;
		} else if (product.isGroundShipping()) {
			classification = Classification.GROUNDSHIP;
		}
		return classification;
	}
}
