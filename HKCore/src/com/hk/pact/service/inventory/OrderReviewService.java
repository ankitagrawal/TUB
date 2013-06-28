package com.hk.pact.service.inventory;

import com.hk.domain.shippingOrder.LineItem;


public interface OrderReviewService {
	
	public void fixLineItem(LineItem lineItem);
	
}
