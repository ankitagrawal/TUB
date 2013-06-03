package com.hk.splitter;

import java.util.Collection;
import java.util.HashMap;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.warehouse.Warehouse;

public class LineItemBucket implements Comparable<LineItemBucket> {
	private CartLineItem lineItem;
	private HashMap<Long, WarehouseBucket> warehouseBucket = new HashMap<Long, WarehouseBucket>();

	public LineItemBucket(CartLineItem lineItem) {
		this.lineItem = lineItem;
	}

	public CartLineItem getLineItem() {
		return lineItem;
	}

	public void addWarehouseBucket(WarehouseBucket bucket) {
		this.warehouseBucket.put(bucket.getWarehouse().getId(), bucket);
	}

	@Override
	public String toString() {
		return "C" + lineItem.getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.lineItem.getId().equals(((LineItemBucket) obj).lineItem.getId());
	}

	@Override
	public int hashCode() {
		return lineItem.getId().hashCode();
	}

	@Override
	public int compareTo(LineItemBucket o) {
		return this.lineItem.getId().compareTo(o.getLineItem().getId());
	}

	public Collection<WarehouseBucket> getAllWarehouseBuckets() {
		return warehouseBucket.values();
	}

	public WarehouseBucket getWarehouseBucket(Warehouse warehouse) {
		return warehouseBucket.get(warehouse.getId());
	}
}
