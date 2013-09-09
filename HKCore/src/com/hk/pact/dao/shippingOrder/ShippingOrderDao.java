package com.hk.pact.dao.shippingOrder;

import com.akube.framework.dao.Page;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.sku.Sku;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

public interface ShippingOrderDao extends BaseDao {

	public ShippingOrder findById(Long shippingOrderId);

	public ShippingOrder findByGatewayOrderId(String gatewayOrderId);

	public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, int pageNo, int perPage);

	public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria);

	public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity);

	public List<ShippingOrder> getShippingOrdersToSendShipmentEmail();

	public Long getBookedQtyOfSkuInQueue(List<Sku> skuList, List<Long> shippingOrderStatus);

	public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId);

	public Long getBookedQtyOfSkuInProcessingQueue(List<Sku> skuList);

    public List<Reason> getReasonForReversePickup(List<Long> listOfReasonIds);
	
}
