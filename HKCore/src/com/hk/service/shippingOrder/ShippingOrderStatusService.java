package com.hk.service.shippingOrder;

import java.util.List;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.dao.shippingOrder.ShippingOrderStatusDao;
import com.hk.domain.order.ShippingOrderStatus;

public interface ShippingOrderStatusService {

    public ShippingOrderStatus find(EnumShippingOrderStatus enumShippingOrderStatus);

    public List<ShippingOrderStatus> getOrderStatuses(List<EnumShippingOrderStatus> enumShippingOrderStatuses);

    public ShippingOrderStatus find(Long shippingOrderStatusId);

    public ShippingOrderStatusDao getShippingOrderStatusDao();

    public void setShippingOrderStatusDao(ShippingOrderStatusDao shippingOrderStatusDao);

}
