package com.hk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.dao.OrderStatusDao;
import com.hk.domain.core.OrderStatus;
import com.hk.service.OrderStatusService;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    private OrderStatusDao orderStatusDao;

    public List<OrderStatus> getOrderStatuses(List<EnumOrderStatus> enumOrderStatus) {
        return getOrderStatusDao().getOrderStatuses(enumOrderStatus);
    }

    public OrderStatus find(EnumOrderStatus enumOrderStatus) {
        return getOrderStatusDao().getOrderStatusById(enumOrderStatus.getId());
    }

    public OrderStatusDao getOrderStatusDao() {
        return orderStatusDao;
    }

    public void setOrderStatusDao(OrderStatusDao orderStatusDao) {
        this.orderStatusDao = orderStatusDao;
    }
    
    

}
