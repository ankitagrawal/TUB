package com.hk.admin.pact.dao.reconciliation;

import com.hk.domain.order.Order;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/12/12
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AdminReconciliationDao extends BaseDao {
	public OrderPaymentReconciliation getOrderPaymentReconciliationBySO(ShippingOrder shippingOrder);

	public OrderPaymentReconciliation getOrderPaymentReconciliationByBaseOrder(Order order);

}
