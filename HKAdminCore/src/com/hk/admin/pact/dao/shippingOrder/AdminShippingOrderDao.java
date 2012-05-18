
package com.hk.admin.pact.dao.shippingOrder;

import java.util.Date;
import java.util.List;


public interface AdminShippingOrderDao {
    
   // public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId);

    public List<Long> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId);
}
