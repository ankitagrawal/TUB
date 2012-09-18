package com.hk.admin.pact.dao.order;

import com.hk.domain.order.Order;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/18/12
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AdminOrderDao {

	public List<Order> getOrdersByGatewayOrderList(List<String> gatewayOrderIdList);

}
