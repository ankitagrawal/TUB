package com.hk.pact.service.order;

import com.hk.domain.order.B2BOrderChecklist;
import com.hk.domain.order.Order;

public interface B2BOrderService {
	
	public boolean checkCForm(Order order);
	
	public void saveB2BOrder(B2BOrderChecklist order);

}
