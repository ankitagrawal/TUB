package com.hk.pact.service.order;

import com.hk.domain.order.B2BOrderCheckList;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

public interface B2BOrderService extends BaseDao {
	
	public boolean checkCForm(Order order);
	
	public void saveB2BOrder(B2BOrderCheckList order);

}
