package com.hk.admin.impl.dao.reconciliation;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.reconciliation.AdminReconciliationDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.BaseDaoImpl;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/12/12
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class AdminReconciliationDaoImpl extends BaseDaoImpl implements AdminReconciliationDao {

	@SuppressWarnings("unchecked")
    public OrderPaymentReconciliation getOrderPaymentReconciliationBySO(ShippingOrder shippingOrder) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrderPaymentReconciliation.class);
		criteria.add(Restrictions.eq("shippingOrder", shippingOrder));
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = findByCriteria(criteria);
		if (orderPaymentReconciliationList != null && orderPaymentReconciliationList.size() > 0) {
			return orderPaymentReconciliationList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
    public OrderPaymentReconciliation getOrderPaymentReconciliationByBaseOrder(Order order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrderPaymentReconciliation.class);
		criteria.add(Restrictions.eq("baseOrder", order));
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = findByCriteria(criteria);
		if (orderPaymentReconciliationList != null && orderPaymentReconciliationList.size() > 0) {
			return orderPaymentReconciliationList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
    public List<OrderPaymentReconciliation> findPaymentDifferenceInCODOrders(Long shippingOrderId, String gatewayOrderId,
	                                                                         Date startDate, Date endDate, Courier courier, String paymentProcess) throws Exception{
		String courierClause        = "";
		String shippingOrderClause  = "" ;
		String gatewayOrderClause   = "";

		String query = "select recon from OrderPaymentReconciliation recon where recon.reconciled = true " +
				" and recon.shippingOrder.shipment.shipDate between :startDate and :endDate " +
				" and recon.reconciledAmount != recon.shippingOrder.amount and recon.paymentProcessType = :paymentProcess ";

		if(shippingOrderId != null) {
			shippingOrderClause = " and recon.shippingOrder.id = '" + shippingOrderId + "' ";
		}

		if(gatewayOrderId != null) {
			gatewayOrderClause = " and recon.shippingOrder.gatewayOrderId = '" + gatewayOrderId + "' ";
		}

		if(courier != null) {
			courierClause = " and recon.shippingOrder.shipment.awb.courier = " + courier;
		}
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = getSession().createQuery(query + shippingOrderClause + gatewayOrderClause + courierClause)
				.setParameter("paymentProcess", paymentProcess).setParameter("startDate", startDate).setParameter("endDate", endDate)
				.list();

		return orderPaymentReconciliationList;
	}

	@SuppressWarnings("unchecked")
    public List<OrderPaymentReconciliation> findPaymentDifferenceInPrepaidOrders(Long baseOrderId, String gatewayOrderId,
	                                                                             Date startDate, Date endDate, String paymentProcess) throws Exception{
		String orderClause          = "" ;
		String gatewayOrderClause   = "";

		String query = "select recon from OrderPaymentReconciliation recon where recon.reconciled = true " +
				" and recon.shippingOrder is null and recon.baseOrder.payment.paymentDate between :startDate and :endDate " +
				" and recon.reconciledAmount != recon.baseOrder.amount and recon.paymentProcessType = :paymentProcess ";

		if(baseOrderId != null) {
			orderClause = " and recon.baseOrder.id = '" + baseOrderId + "' ";
		}

		if(gatewayOrderId != null) {
			gatewayOrderClause = " and recon.baseOrder.gatewayOrderId = '" + gatewayOrderId + "' ";
		}

		List<OrderPaymentReconciliation> orderPaymentReconciliationList = getSession().createQuery(query + orderClause + gatewayOrderClause)
				.setParameter("paymentProcess", paymentProcess).setParameter("startDate", startDate).setParameter("endDate", endDate).list();

		return orderPaymentReconciliationList;
	}

}
