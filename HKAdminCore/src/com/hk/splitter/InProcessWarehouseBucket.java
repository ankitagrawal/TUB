package com.hk.splitter;

import java.util.LinkedList;

import com.hk.domain.warehouse.Warehouse;

public class InProcessWarehouseBucket extends WarehouseBucket {
	private LinkedList<LineItemBucket> list;

	private InProcessWarehouseBucket(Warehouse warehouse) {
		super(warehouse);
	}

	public InProcessWarehouseBucket(WarehouseBucket warehouseBucket) {
		this(warehouseBucket.getWarehouse());
		super.lineItemBuckets = warehouseBucket.lineItemBuckets;
		list = new LinkedList<LineItemBucket>();
		list.addAll(getAllLineItemBuckets());
	}

	public LineItemBucket removeLast() {
		return list.removeLast();
	}
}
