package com.hk.api.rest.pact.service;

import com.hk.api.rest.models.order.APIOrder;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 4, 2012
 * Time: 3:19:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface APIOrderService {

  public String createOrderInHK(APIOrder apiOrder);

  public String trackOrder(String orderId);

}
