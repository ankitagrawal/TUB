package com.hk.api.pact.service;

import com.hk.api.dto.order.HKAPIOrderDTO;
import com.hk.domain.order.Order;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 4, 2012
 * Time: 3:19:51 PM
 */
public interface APIOrderService {


    public String createOrderInHK(HKAPIOrderDTO HKAPIOrderDTO);

    @Deprecated
    public String trackOrder(String orderId);

    public String createOrderInHk(Order order);

}
