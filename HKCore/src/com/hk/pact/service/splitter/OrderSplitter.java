package com.hk.pact.service.splitter;

import java.util.Collection;
import java.util.Set;

import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;

public interface OrderSplitter {
	
	Set<ShippingOrder> split(long orderId);

}
