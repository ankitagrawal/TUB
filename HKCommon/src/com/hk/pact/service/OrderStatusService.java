package com.hk.pact.service;

import java.util.List;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.core.OrderStatus;

public interface OrderStatusService {

    public List<OrderStatus> getOrderStatuses(List<EnumOrderStatus> enumOrderStatus);

    public OrderStatus find(EnumOrderStatus enumOrderStatus);

}
