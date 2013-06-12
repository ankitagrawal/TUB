package com.hk.splitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.splitter.LineItemContainer.Classification;

public class LineItemClassification {
	private String id;
	private Classification classification;

	private Map<Long, WarehouseBucket> warehouseBuckets = new HashMap<Long, WarehouseBucket>();
	private Map<Long, LineItemBucket> lineItemBuckets = new HashMap<Long, LineItemBucket>();
	
	private Map<Integer, List<InProcessWarehouseBucket>> allPossibleBuckets = new HashMap<Integer, List<InProcessWarehouseBucket>>();
	private int iteration = 0;

	public LineItemClassification(String id, Classification classification) {
		this.id = id;
		this.classification = classification;
	}
	
	public void addLineItem(Warehouse warehouse, CartLineItem cartLineItem) {
		WarehouseBucket warehouseBucket = warehouseBuckets.get(warehouse.getId());
		if (warehouseBucket == null) {
			warehouseBucket = new WarehouseBucket(warehouse);
		}
		warehouseBuckets.put(warehouse.getId(), warehouseBucket);

		LineItemBucket lineItemBucket = lineItemBuckets.get(cartLineItem.getId());
		if (lineItemBucket == null) {
			lineItemBucket = new LineItemBucket(cartLineItem);
		}
		lineItemBuckets.put(cartLineItem.getId(), lineItemBucket);

		warehouseBucket.addLineItemBucket(lineItemBucket);
		lineItemBucket.addWarehouseBucket(warehouseBucket);
	}
	
	public Collection<LineItemBucket> getLineItemBuckets() {
		return lineItemBuckets.values();
	}

	public Classification getClassification() {
		return classification;
	}

	public String getId() {
		return id;
	}

	public Collection<UniqueWhCombination> generatePerfactCombinations() {
		LinkedList<WarehouseBucket> warehouseBucketList = new LinkedList<WarehouseBucket>(this.warehouseBuckets.values());
		Collections.sort(warehouseBucketList);
		for (int i = 0; i < warehouseBucketList.size(); i++) {
			WarehouseBucket bucket = warehouseBucketList.removeFirst();
			warehouseBucketList.addLast(bucket);
			doProcessInternal(warehouseBucketList);
		}
		Collections.reverse(warehouseBucketList);
		for (int i = 0; i < warehouseBucketList.size(); i++) {
			WarehouseBucket bucket = warehouseBucketList.removeFirst();
			warehouseBucketList.addLast(bucket);
			doProcessInternal(warehouseBucketList);
		}
		
		MinWhCombination combination = new MinWhCombination();
		combination.process(allPossibleBuckets);
		return combination.getMinWhCombinations();
	}

	private void doProcessInternal(List<WarehouseBucket> warehouseBucketList) {
		Set<WarehouseBucket> uniqueBuckets = new HashSet<WarehouseBucket>();
		
		int pointer = 0;
		for (int i = 0; i < warehouseBucketList.size(); i++) {
			int stroredPointer = pointer;
			InProcessWarehouseBucket pointedBucket = null;
			WarehouseBucket originalBucket = null;

			Collection<LineItemBucket> uniqueLiBuckets = new HashSet<LineItemBucket>();

			if (i == 0) {
				originalBucket = warehouseBucketList.get(i);
				pointedBucket = new InProcessWarehouseBucket(originalBucket.clone());
			} else {
				List<InProcessWarehouseBucket> lastIterationBucket = getLastIterationBucket();
				for (int j = 0; j < stroredPointer ; j++) {
					addInProcessBucket(lastIterationBucket.get(j));
				}
				pointedBucket = new InProcessWarehouseBucket(lastIterationBucket.get(pointer).clone());
				originalBucket = this.warehouseBuckets.get(pointedBucket.getWarehouse().getId());
			}
			addInProcessBucket(pointedBucket);
			boolean added = uniqueBuckets.add(pointedBucket);
			
			if(!added) {
				LineItemBucket lBucket = null;
				try {
					lBucket = pointedBucket.removeLast();
					if(originalBucket.getLineItemBucket(lBucket.getLineItem()).getAllWarehouseBuckets().size() > 1) {
						pointedBucket.remove(lBucket);
					}
				} catch (NoSuchElementException e) {
					pointer++;
				}
			}
			
			uniqueLiBuckets.addAll(pointedBucket.getAllLineItemBuckets());

			for (int j = stroredPointer + 1; j < warehouseBucketList.size(); j++) {
				WarehouseBucket nextOriginalBucket = warehouseBucketList.get(j);
				InProcessWarehouseBucket cBucket = new InProcessWarehouseBucket(nextOriginalBucket.clone());

				cBucket.remove(uniqueLiBuckets);
				uniqueLiBuckets.addAll(nextOriginalBucket.getAllLineItemBuckets());

				addInProcessBucket(cBucket);
			}
			iteration++;
			
			//System.out.println(getLastIterationBucket());
		}
	}
	
	private void addInProcessBucket(InProcessWarehouseBucket bucket) {
		List<InProcessWarehouseBucket> buckets = allPossibleBuckets.get(iteration);
		if(buckets == null) {
			buckets = new ArrayList<InProcessWarehouseBucket>();
			allPossibleBuckets.put(iteration, buckets);
		}
		buckets.add(bucket);
	}
	
	private List<InProcessWarehouseBucket> getLastIterationBucket() {
		return allPossibleBuckets.get(iteration-1);
	}
	
	private static class MinWhCombination {
		private Set<UniqueWhCombination> combinations = new TreeSet<UniqueWhCombination>();
		int minWh = Integer.MAX_VALUE;
		
		public void process(Map<Integer,List<InProcessWarehouseBucket>> allPossibleBuckets) {
			for (List<InProcessWarehouseBucket> list : allPossibleBuckets.values()) {
				UniqueWhCombination cmb = new UniqueWhCombination();
				for (InProcessWarehouseBucket bkt : list) {
					if(bkt.getAllLineItemBuckets().size() > 0) {
						cmb.buckets.add(bkt);
					}
				}
				int size = cmb.buckets.size();
				if(size < minWh) {
					minWh = size;
					combinations.clear();
					combinations.add(cmb);
				} else if(size == minWh) {
					combinations.add(cmb);
				}
			}
		}
		
		public Set<UniqueWhCombination> getMinWhCombinations() {
			return combinations;
		}
		
	}
	
	public static class UniqueWhCombination implements Comparable<UniqueWhCombination> {
		private Set<WarehouseBucket> buckets = new TreeSet<WarehouseBucket>();

		@Override
		public String toString() {
			return buckets.toString();
		}
		
		@Override
		public int compareTo(UniqueWhCombination o) {
			return this.toString().compareTo(o.toString());
		}
		
		public Collection<WarehouseBucket> getBuckets() {
			return buckets;
		}
	}
}