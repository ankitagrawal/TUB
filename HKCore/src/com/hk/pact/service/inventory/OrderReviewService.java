package com.hk.pact.service.inventory;

import com.hk.domain.shippingOrder.LineItem;


public interface OrderReviewService {
	
	public boolean fixLineItem(LineItem lineItem);
	
}
