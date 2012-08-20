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

    // Getting comma seperated string for the duplicated
    public static String convertListToString(List<String> list) {
        StringBuffer strBuffr = new StringBuffer();
        for (String string : new HashSet<String>(list) ) {
            strBuffr.append(string);
            strBuffr.append(",");
        }
        return strBuffr.toString();
    }

    public static Map<ConsignmentStatus, ConsignmentLifecycleStatus> getStatusMapForConsignmentTracking(BaseDao baseDao){
        Map<ConsignmentStatus,ConsignmentLifecycleStatus> statusMap = new HashMap<ConsignmentStatus,ConsignmentLifecycleStatus>();
        statusMap.put(baseDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentOutForDelivery),
                baseDao.get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.Dispatched));
        statusMap.put(baseDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentOnHold),
                baseDao.get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.Hold));
        statusMap.put(baseDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentDelivered),
                baseDao.get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.Delivered));
        statusMap.put(baseDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentDamaged),
                baseDao.get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.Damaged));
        statusMap.put(baseDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentRTO),
                baseDao.get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.ReturnedToHub));
        return null;
    }
}
