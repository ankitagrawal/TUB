package com.hk.admin.impl.dao.shippingOrder;

import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class AdminShippingOrderDaoImpl extends BaseDaoImpl implements AdminShippingOrderDao {

	@SuppressWarnings("unchecked")
//    public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId) {
//
//        if (startDate == null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, -1);
//            startDate = calendar.getTime();
//        }
//        if (endDate == null) {
//            endDate = new Date();
//        }
//        String query = "select distinct so.id  " + " from Shipment shipment, ShippingOrder so where " + " so.shipment = shipment" + " and shipment.courier.id = :courierId "
//                + " and shipment.shipDate between :startDate and :endDate " + " and shipment.deliveryDate is null ";
//        return getSession().createQuery(query).setParameter("courierId", courierId).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
//    }

	public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierIdList) {

		if (startDate == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			startDate = calendar.getTime();
		}
		if (endDate == null) {
			endDate = new Date();
		}
		String query = "select distinct so  " + " from Shipment shipment, ShippingOrder so where " + " so.shipment = shipment" + " and shipment.awb.courier.id in (:courierIdList) "
				+ " and shipment.shipDate between :startDate and :endDate " + " and shipment.deliveryDate is null";
		return getSession().createQuery(query).setParameterList("courierIdList", courierIdList).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
	}

	public List<ShippingOrder> getShippingOrderByGatewayOrderList(List<String> gatewayOrderIdList) {
		List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
		if(gatewayOrderIdList != null && gatewayOrderIdList.size() > 0) {
			DetachedCriteria criteria = DetachedCriteria.forClass(ShippingOrder.class);
					criteria.add(Restrictions.in("gatewayOrderId", gatewayOrderIdList));
					shippingOrderList = findByCriteria(criteria);
		}
		return shippingOrderList;
	}

}
