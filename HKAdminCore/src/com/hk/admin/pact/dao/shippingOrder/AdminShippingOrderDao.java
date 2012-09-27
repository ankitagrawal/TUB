
package com.hk.admin.pact.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.domain.order.ShippingOrder;


public interface AdminShippingOrderDao {
    
   // public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId);

    public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId);

	public List<ShippingOrder> getShippingOrderByGatewayOrderList(List<String> gatewayOrderIdList);

}
