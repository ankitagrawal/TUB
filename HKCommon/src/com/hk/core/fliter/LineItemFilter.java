package com.hk.core.fliter;

import java.util.HashSet;
import java.util.Set;

import com.hk.domain.shippingOrder.LineItem;

/**
 * @author vaibhav.adlakha
 */
public class LineItemFilter {

	private Set<LineItem> lineItems;
	//private Set<EnumLineItemType> lineItemTypes = new HashSet<EnumLineItemType>();
	private Long skuId;
	private Long cartLineItemConfigId;

	public LineItemFilter(Set<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

/*
	public LineItemFilter setLineItemTypes(Set<EnumLineItemType> lineItemTypes) {
		this.lineItemTypes = lineItemTypes;
		return this;
	}

	public LineItemFilter addLineItemType(EnumLineItemType lineItemType) {
		this.lineItemTypes.add(lineItemType);
		return this;
	}
*/

	public LineItemFilter setSkuId(Long skuId) {
		this.skuId = skuId;
		return this;
	}

	public LineItemFilter setCartLineItemConfigId(Long cartLineItemConfigId) {
		this.cartLineItemConfigId = cartLineItemConfigId;
		return this;
	}

	public Set<LineItem> filter() {
		Set<LineItem> currentLineItems = new HashSet<LineItem>(lineItems);
		Set<LineItem> filterLineItems = new HashSet<LineItem>();
		/*if (lineItemTypes != null && lineItemTypes.size() > 0) {

			List<Long> selectedLineItemTypeIDs = EnumLineItemType.getLineItemTypeIDs(lineItemTypes);

			for (LineItem lineItem : lineItems) {
				if (!selectedLineItemTypeIDs.contains(lineItem.getLineItemType().getId())) {
					currentLineItems.remove(lineItem);
				}
			}
		}*/

		filterLineItems.addAll(currentLineItems);

		if (skuId != null) {
			for (LineItem lineItem : currentLineItems) {
				if (!skuId.equals(lineItem.getSku().getId())) {
					filterLineItems.remove(lineItem);
				}
			}
		}
		currentLineItems.clear();
		currentLineItems.addAll(filterLineItems);

		if (cartLineItemConfigId != null) {
			for (LineItem lineItem : currentLineItems) {
				if (!cartLineItemConfigId.equals(lineItem.getCartLineItem().getCartLineItemConfig().getId())) {
					filterLineItems.remove(lineItem);
				}
			}
		}

		return filterLineItems;
	}

	/*public Set<LineItem> filterLineItemsByType(EnumLineItemType lineItemType) {
		return filterLineItemsByType(Arrays.asList(lineItemType));
	}

	public Set<LineItem> filterLineItemsBySku(Long skuId) {
		Set<LineItem> filteredLineItems = new HashSet<LineItem>();

		for (LineItem lineItem : lineItems) {

		}

		return filteredLineItems;
	}

	public Set<LineItem> filterLineItemsByType(List<EnumLineItemType> selectedLineItemTypes) {
		Set<LineItem> filteredLineItems = new HashSet<LineItem>();


		return filteredLineItems;
	}*/
}
