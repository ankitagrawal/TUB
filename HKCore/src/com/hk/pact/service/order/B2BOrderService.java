package com.hk.pact.service.order;

import com.hk.domain.order.B2bOrderChecklist;
import com.hk.domain.order.Order;

public interface B2BOrderService {
	
	public boolean checkCForm(Order order);
	
	public void saveB2BOrder(B2bOrderChecklist order);

}
