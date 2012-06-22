
package com.hk.admin.pact.dao.shippingOrder;

import com.hk.domain.order.ShippingOrder;

import java.util.Date;
import java.util.List;


public interface AdminShippingOrderDao {
    
   // public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId);

    public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId);

}
