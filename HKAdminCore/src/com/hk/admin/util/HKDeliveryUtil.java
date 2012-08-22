package com.hk.admin.util;

import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import com.hk.pact.dao.BaseDao;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;

import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;


public class HKDeliveryUtil {

    static Map<String,Long> consignmentStatusMap = new HashMap();



    // Getting comma seperated string for the duplicated
    public static String convertListToString(List<String> list) {
        StringBuffer strBuffr = new StringBuffer();
        for (String string : new HashSet<String>(list) ) {
            strBuffr.append(string);
            strBuffr.append(",");
        }
        return strBuffr.toString();
    }

    static{
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentOutForDelivery.getStatus(), EnumConsignmentLifecycleStatus.Dispatched.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentOnHold.getStatus(), EnumConsignmentLifecycleStatus.Hold.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentDelivered.getStatus(), EnumConsignmentLifecycleStatus.Delivered.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentDamaged.getStatus(), EnumConsignmentLifecycleStatus.Damaged.getId());
        consignmentStatusMap.put(EnumConsignmentStatus.ShipmentRTO.getStatus(), EnumConsignmentLifecycleStatus.ReturnedToHub.getId());
    }

    public static Long getLifcycleStatusIdFromConsignmentStatus(String consignmentStatus){
        return consignmentStatusMap.get(consignmentStatus);
    }
}
