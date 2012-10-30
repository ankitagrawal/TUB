package com.hk.pact.service.order;

import com.hk.domain.order.Order;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserOrderService {
    List<Order> getUserOrders(String emailId);

    List<Order> getUserOrders(long phone);
}
