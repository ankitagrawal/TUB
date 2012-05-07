
package com.hk.admin.impl.dao.shippingOrder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class AdminShippingOrderDaoImpl extends BaseDaoImpl implements AdminShippingOrderDao {
    
    @SuppressWarnings("unchecked")
    public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId) {

        if (startDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            startDate = calendar.getTime();
        }
        if (endDate == null) {
            endDate = new Date();
        }
        String query = "select distinct so.id  " + " from Shipment shipment, ShippingOrder so where " + " so.shipment = shipment" + " and shipment.courier.id = :courierId "
                + " and shipment.shipDate between :startDate and :endDate " + " and shipment.deliveryDate is null ";
        return getSession().createQuery(query).setParameter("courierId", courierId).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
    }

}
