package com.hk.admin.pact.dao.order;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;

import java.util.Date;
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

  public List<Order> findSaleForTimeFrame(Long storeId, Date startDate, Date endDate, List<OrderStatus> orderStatusList);

}
