package com.hk.pact.service.inventory;

import com.hk.domain.shippingOrder.LineItem;


public interface OrderReviewService {
	
	public void fixLineItem(LineItem lineItem) throws CouldNotFixException;
	
	public static class CouldNotFixException extends Exception {

		private static final long serialVersionUID = 1L;

		public CouldNotFixException(String message) {
			super(message);
		}
	}
}
