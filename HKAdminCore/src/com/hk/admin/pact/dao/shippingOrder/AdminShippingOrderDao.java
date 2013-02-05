package com.hk.admin.pact.dao.shippingOrder;

import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;


public interface AdminShippingOrderDao extends BaseDao{

	// public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId);

	public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId);

	public List<ShippingOrder> getShippingOrderByGatewayOrderList(List<String> gatewayOrderIdList);

}
