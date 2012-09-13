package com.hk.admin.impl.dao.reconciliation;

import com.hk.admin.pact.dao.reconciliation.AdminReconciliationDao;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/12/12
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class AdminReconciliationDaoImpl extends BaseDaoImpl implements AdminReconciliationDao {

	public OrderPaymentReconciliation getOrderPaymentReconciliationBySO(ShippingOrder shippingOrder) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrderPaymentReconciliation.class);
		criteria.add(Restrictions.eq("shippingOrder", shippingOrder));
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = findByCriteria(criteria);
		if (orderPaymentReconciliationList != null && orderPaymentReconciliationList.size() > 0) {
			return orderPaymentReconciliationList.get(0);
		}
		return null;
	}

	public OrderPaymentReconciliation getOrderPaymentReconciliationByBaseOrder(Order order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrderPaymentReconciliation.class);
		criteria.add(Restrictions.eq("baseOrder", order));
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = findByCriteria(criteria);
		if (orderPaymentReconciliationList != null && orderPaymentReconciliationList.size() > 0) {
			return orderPaymentReconciliationList.get(0);
		}
		return null;
	}


}
