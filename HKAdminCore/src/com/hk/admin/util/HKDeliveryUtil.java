package com.hk.admin.util;

import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;

import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;


public class HKDeliveryUtil {

    static Map<String,Long> consignmentStatusMap = new HashMap<String,Long>();



    // Getting comma seperated string for the duplicated
    public static String convertListToString(List<String> list) {
        StringBuffer strBuffr = new StringBuffer();
        if(list.size()>0){
        for (String string : new HashSet<String>(list) ) {
            strBuffr.append(string);
            strBuffr.append(",");
        }
        }
        return strBuffr.toString();
    }

    static{
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentOutForDelivery.getStatus(), EnumConsignmentLifecycleStatus.Dispatched.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentOnHold.getStatus(), EnumConsignmentLifecycleStatus.Hold.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentDelivered.getStatus(), EnumConsignmentLifecycleStatus.Delivered.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentDamaged.getStatus(), EnumConsignmentLifecycleStatus.Damaged.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentRTH.getStatus(), EnumConsignmentLifecycleStatus.ReturnedToHub.getId());

        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentOnHoldByCustomer.getStatus(), EnumConsignmentLifecycleStatus.OnHoldByCustomer.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentReceivedAtHub.getStatus(), EnumConsignmentLifecycleStatus.ReceivedAtHub.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentLost.getStatus(), EnumConsignmentLifecycleStatus.ConsignmentLost.getId());
    }

    public static Long getLifcycleStatusIdFromConsignmentStatus(String consignmentStatus){
        return consignmentStatusMap.get(consignmentStatus);
    }
}
