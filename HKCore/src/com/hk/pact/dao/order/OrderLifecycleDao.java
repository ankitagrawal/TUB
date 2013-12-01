package com.hk.pact.dao.order;

import java.util.List;

import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderLifecycle;
import com.hk.pact.dao.BaseDao;

public interface OrderLifecycleDao extends BaseDao {

    public Long containsOrderLifeCycleActivity(Order order, OrderLifecycleActivity orderLifecycleActivity);

    public OrderLifecycle getLatestOrderActivityByActvityType(Order order, List<Long> activityIds);

}
