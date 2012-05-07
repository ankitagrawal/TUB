package com.hk.pact.dao.shippingOrder;

import java.util.List;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.dao.BaseDao;

public interface ShippingOrderStatusDao extends BaseDao {

    public List<ShippingOrderStatus> getOrderStatuses(List<EnumShippingOrderStatus> enumShippingOrderStatuses);

}
