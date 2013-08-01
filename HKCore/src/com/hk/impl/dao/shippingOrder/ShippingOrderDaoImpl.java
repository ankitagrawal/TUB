package com.hk.impl.dao.shippingOrder;

import com.akube.framework.dao.Page;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
@SuppressWarnings("unchecked")
public class ShippingOrderDaoImpl extends BaseDaoImpl implements ShippingOrderDao {

	public ShippingOrder findById(Long shippingOrderId) {
		return get(ShippingOrder.class, shippingOrderId);
	}

	public ShippingOrder findByGatewayOrderId(String gatewayOrderId) {
		return (ShippingOrder) getSession().createQuery("from ShippingOrder o where o.gatewayOrderId = :gatewayOrderId").setString("gatewayOrderId", gatewayOrderId).uniqueResult();
	}

	public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, int pageNo, int perPage) {
		DetachedCriteria searchCriteria = shippingOrderSearchCriteria.getSearchCriteria();
		//searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return list(searchCriteria, true, pageNo, perPage);
	}

	public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria) {
		DetachedCriteria searchCriteria = shippingOrderSearchCriteria.getSearchCriteria();
		searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//searchCriteria.setMaxResults(1000);
		//TODO:rewrite
		return findByCriteria(searchCriteria);
	}

	public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity) {
		return get(ShippingOrderLifeCycleActivity.class, enumShippingOrderLifecycleActivity.getId());
	}

	public List<ShippingOrder> getShippingOrdersToSendShipmentEmail() {
		StringBuilder hqlQuery = new StringBuilder("select DISTINCT so from ShippingOrder so where so.shippingOrderStatus.id = :shippedStatus and so.shipment.isEmailSent =false");

		Query shippingOrderListQuery = getSession().createQuery(hqlQuery.toString()).setParameter("shippedStatus", EnumShippingOrderStatus.SO_Shipped.getId());

		return (List<ShippingOrder>) shippingOrderListQuery.list();
	}

	public Long getBookedQtyOfSkuInQueue(List<Sku> skuList) {
		Long qtyInQueue = 0L;
		if (skuList != null && !skuList.isEmpty()) {
			String query = "select sum(li.qty) from LineItem li " + "where li.sku in (:skuList) " + "and li.shippingOrder.shippingOrderStatus.id in (:orderStatusIdList) ";
			qtyInQueue = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameterList("orderStatusIdList",
					EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory())).uniqueResult();
			if (qtyInQueue == null) {
				qtyInQueue = 0L;
			}
		}
		return qtyInQueue;
	}

	/**
	 * @param sku based on warehouse
	 * @return Sum of Qty of lineitems for product variant which are not yet shipped
	 */
	public Long getBookedQtyOfSkuInQueue(Sku sku) {
		return getBookedQtyOfSkuInQueue(Arrays.asList(sku));
	}

	@SuppressWarnings("unchecked")
	public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId) {

		String query = "select distinct so.id  " + " from Shipment shipment, ShippingOrder so where " + " so.shipment = shipment" + " and shipment.awb.courier.id = :courierId "
				+ " and shipment.shipDate between :startDate and :endDate " + " and shipment.deliveryDate is null ";
		return getSession().createQuery(query).setParameter("courierId", courierId).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
	}

	public Long getBookedQtyOfSkuInProcessingQueue(List<Sku> skuList) {
		Long qtyInQueue = 0L;
		if (skuList != null && !skuList.isEmpty()) {
			String query = "select sum(li.qty) from LineItem li " + "where li.sku in (:skuList) " + "and li.shippingOrder.shippingOrderStatus.id in (:orderStatusIdList) ";
			qtyInQueue = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameterList("orderStatusIdList",
					EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventoryInProcessingQueue())).uniqueResult();
			if (qtyInQueue == null) {
				qtyInQueue = 0L;
			}
		}
		return qtyInQueue;
	}

    public List<Reason> getReasonForReversePickup(List<Long> listOfReasonIds) {
        String query = " from Reason where id in (:reasonList) ";
        return getSession().createQuery(query).setParameterList("reasonList", listOfReasonIds).list();
    }


}
