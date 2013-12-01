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
        shippingOrderStatusMap.put(EnumShippingOrderStatus.SO_Shipped.getName(), Arrays.asList(EnumShippingOrderStatus.SO_Delivered,EnumShippingOrderStatus.RTO_Initiated));
        shippingOrderStatusMap.put(EnumShippingOrderStatus.SO_Delivered.getName(),Arrays.asList(EnumShippingOrderStatus.SO_Shipped,EnumShippingOrderStatus.RTO_Initiated));
        shippingOrderStatusMap.put(EnumShippingOrderStatus.RTO_Initiated.getName(),Arrays.asList(EnumShippingOrderStatus.SO_Shipped, EnumShippingOrderStatus.SO_RTO));
        shippingOrderStatusMap.put(EnumShippingOrderStatus.SO_RTO.getName(),Arrays.asList(EnumShippingOrderStatus.SO_Shipped));
        shippingOrderStatusMap.put(EnumShippingOrderStatus.SO_Customer_Return_Replaced.getName(),Arrays.asList(EnumShippingOrderStatus.RTO_Initiated));
        shippingOrderStatusMap.put(EnumShippingOrderStatus.SO_Customer_Return_Refunded.getName(),Arrays.asList(EnumShippingOrderStatus.RTO_Initiated));
        shippingOrderStatusMap.put(EnumShippingOrderStatus.SO_ReversePickup_Initiated.getName(),Arrays.asList(EnumShippingOrderStatus.RTO_Initiated));
        return shippingOrderStatusMap;
    }
}
