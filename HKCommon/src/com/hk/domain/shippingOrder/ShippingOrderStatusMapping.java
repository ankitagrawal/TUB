package com.hk.domain.shippingOrder;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 4/1/13
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingOrderStatusMapping {
    Map<String, List> shippingOrderStatusMap = new HashMap<String, List>();

    public Map<String, List> getShippingOrderStatusMap(){
        shippingOrderStatusMap.put("SO Shipped", Arrays.asList(EnumShippingOrderStatus.SO_Delivered,EnumShippingOrderStatus.RTO_Initiated));
        shippingOrderStatusMap.put("SO Delivered",Arrays.asList(EnumShippingOrderStatus.SO_Shipped,EnumShippingOrderStatus.RTO_Initiated));
        shippingOrderStatusMap.put("RTO Initiated",Arrays.asList(EnumShippingOrderStatus.SO_Shipped));
        shippingOrderStatusMap.put("SO RTO",Arrays.asList(EnumShippingOrderStatus.SO_Shipped));
        return shippingOrderStatusMap;
    }
}
