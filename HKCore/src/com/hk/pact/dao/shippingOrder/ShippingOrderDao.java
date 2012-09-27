package com.hk.pact.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.sku.Sku;
import com.hk.pact.dao.BaseDao;

public interface ShippingOrderDao extends BaseDao {

    public ShippingOrder findById(Long shippingOrderId);

     public ShippingOrder findByGatewayOrderId(String gatewayOrderId);

    public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, int pageNo, int perPage);

    public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria);

    public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity);

    public List<ShippingOrder> getShippingOrdersToSendShipmentEmail();

    public Long getBookedQtyOfSkuInQueue(List<Sku> skuList);

    public Long getBookedQtyOfSkuInProcessingQueue(List<Sku> skuList);

    /**
     * @param sku based on warehouse
     * @return Sum of Qty of lineitems for product variant which are not yet shipped
     */
    public Long getBookedQtyOfSkuInQueue(Sku sku);

    public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId);
}
