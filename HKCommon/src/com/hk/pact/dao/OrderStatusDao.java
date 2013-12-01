package com.hk.pact.dao;

import java.util.List;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.core.OrderStatus;

public interface OrderStatusDao extends BaseDao {

    
    public OrderStatus getOrderStatusById(Long orderStatusId);
    
    public List<OrderStatus> getOrderStatuses(List<EnumOrderStatus> enumOrderStatus) ;
    
    public List<OrderStatus> listOrderStatusForReporting() ;
      

}
