package com.hk.impl.service.order;

import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.dao.user.UserDetailDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserOrderServiceImpl implements UserOrderService {

    final int MAX_ORDER_COUNT = 50;
    @Autowired
    UserDetailDao userDetailsDao;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    public List<Order> getUserOrders(String emailId){
        User user = userService.findByEmail(emailId).get(0);
        if (user != null){
            return (List<Order>)orderService.listOrdersForUser(user, 1, MAX_ORDER_COUNT).getList();
        }
        return new ArrayList<Order>();
    }

    public List<Order> getUserOrders(long phone){
        List<UserDetail> userDetail = userDetailsDao.findByPhone(phone);
        if ((userDetail != null) && (userDetail.size() == 1)){
            //Maximum of 50 orders for now
            return (List<Order>)orderService.listOrdersForUser(userDetail.get(0).getUser(), 1, MAX_ORDER_COUNT).getList();
        }
        return new ArrayList<Order>();
    }
}
