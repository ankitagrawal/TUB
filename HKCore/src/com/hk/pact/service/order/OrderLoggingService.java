package com.hk.pact.service.order;

import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;

public interface OrderLoggingService {
    
    public OrderLifecycleActivity getOrderLifecycleActivity(EnumOrderLifecycleActivity enumOrderLifecycleActivity);

    public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity);

    public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments);

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments);
}
