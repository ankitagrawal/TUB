package com.hk.splitter;

import java.util.Collection;
import java.util.LinkedHashMap;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.warehouse.Warehouse;

public class WarehouseBucket implements Comparable<WarehouseBucket>, Cloneable {
	private Warehouse warehouse;
	protected LinkedHashMap<Long, LineItemBucket> lineItemBuckets = new LinkedHashMap<Long, LineItemBucket>();

	public WarehouseBucket(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void addLineItemBucket(LineItemBucket bucket) {
		lineItemBuckets.put(bucket.getLineItem().getId(), bucket);
	}
	
	@Override
	public String toString() {
		return "W" + warehouse.getId() + '=' + lineItemBuckets.values();
	}

	@Override
	public boolean equals(Object obj) {
		return this.warehouse.getId().equals(((WarehouseBucket) obj).getWarehouse().getId());
	}

	@Override
	public int hashCode() {
		return warehouse.getId().hashCode();
	}

	@Override
	public int compareTo(WarehouseBucket o) {
		int result = this.warehouse.getId().compareTo(o.getWarehouse().getId());
		if (result == 0) {
			result = Integer.valueOf(this.lineItemBuckets.size()).compareTo(o.lineItemBuckets.size());
		}
		if(result == 0) {
			result = lineItemBuckets.values().toString().compareTo(o.lineItemBuckets.toString());
		}
		return result;
	}

	public Collection<LineItemBucket> getAllLineItemBuckets() {
		return lineItemBuckets.values();
	}

	public LineItemBucket getLineItemBucket(CartLineItem cartLineItem) {
		return lineItemBuckets.get(cartLineItem.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public WarehouseBucket clone() {
		WarehouseBucket bucket = new WarehouseBucket(warehouse);
		bucket.lineItemBuckets = (LinkedHashMap<Long, LineItemBucket>) this.lineItemBuckets.clone();
		return bucket;
	}

	public void remove(Collection<LineItemBucket> buckets) {
		for (LineItemBucket lineItemBucket : buckets) {
			remove(lineItemBucket);
		}
	}

	public void remove(LineItemBucket bucket) {
		lineItemBuckets.remove(bucket.getLineItem().getId());
	}
}
