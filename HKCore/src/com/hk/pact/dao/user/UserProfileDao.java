package com.hk.pact.dao.user;

import java.util.List;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface UserProfileDao extends BaseDao{

    public List<Order> getOrdersForUserSortedByDate(List<OrderStatus> orderStatusList, User user);

}
