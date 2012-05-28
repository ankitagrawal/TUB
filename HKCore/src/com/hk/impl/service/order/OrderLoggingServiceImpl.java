package com.hk.impl.service.order;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderLifecycle;
import com.hk.domain.user.User;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderLoggingService;


@Service
public class OrderLoggingServiceImpl implements OrderLoggingService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserService userService;
    
    public OrderLifecycleActivity getOrderLifecycleActivity(EnumOrderLifecycleActivity enumOrderLifecycleActivity) {
        return getOrderDao().get(OrderLifecycleActivity.class, enumOrderLifecycleActivity.getId());
    }

    public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity) {
         User user;
        if(order.getStore().getId()== 1L){
            user  = getUserService().getLoggedInUser();
        }
        else{
            user=order.getUser();
        }
        OrderLifecycleActivity orderLifecycleActivity = getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, null);
    }

    public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments) {
        User user = getUserService().getAdminUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments) {
        OrderLifecycle orderLifecycle = new OrderLifecycle();
        orderLifecycle.setOrder(order);
        orderLifecycle.setOrderLifecycleActivity(orderLifecycleActivity);
        orderLifecycle.setUser(user);
        if (StringUtils.isNotBlank(comments)) {
            orderLifecycle.setComments(comments);
        }
        orderLifecycle.setActivityDate(new Date());
        getOrderDao().save(orderLifecycle);
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    
}
