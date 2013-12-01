//package com.hk.db.seed.courier;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import org.springframework.stereotype.Component;
//
//import com.hk.constants.courier.EnumCourier;
//import com.hk.constants.courier.EnumCourierGroupHasCourier;
//import com.hk.db.seed.BaseSeedData;
//import com.hk.domain.courier.Courier;
//import com.hk.domain.courier.CourierGroup;
//
///**
// * User: Pratham
// * Time: 29 Jun, 2010 3:09:49 PM
// */
//@Component
//public class CourierGroupHasCourierSeedData extends BaseSeedData {
//
//    public void insert(java.lang.String name, List<EnumCourier> enumCouriers) {
//        CourierGroup courierGroup = (CourierGroup) getBaseDao().findUniqueByNamedParams("from CourierGroup c where c.name = :name", new String[]{"name"}, new Object[]{name});
//        if (courierGroup == null) {
//            throw new RuntimeException("CourierGroup " + name + " not found while inserting CourierGroupHasCourier");
//        }
//        for (EnumCourier enumCourier : enumCouriers) {
//            Courier courier = getBaseDao().get(Courier.class, enumCourier.getId());
//            if (courier == null) {
//                throw new RuntimeException("Error while inserting courier " + enumCourier.getName() + " for courier group " + name);
//            }
//            courierGroup.getCouriers().add(courier);
//        }
//        save(courierGroup);
//    }
//
//    public void invokeInsert() {
//
//        // removing any existing courier group courier mappings
//        for (CourierGroup courierGroup : getBaseDao().getAll(CourierGroup.class)) {
//            courierGroup.setCouriers(new HashSet<Courier>(0));
//            save(courierGroup);
//        }
//
//        // removing any existing role permission mappings
//        for (CourierGroup courierGroup : getBaseDao().getAll(CourierGroup.class)) {
//            courierGroup.setCouriers(new HashSet<Courier>(0));
//            save(courierGroup);
//        }
//
//        List<String> pkList = new ArrayList<String>();
//
//        for (EnumCourierGroupHasCourier courierGroupHasCourier : EnumCourierGroupHasCourier.values()) {
//
//            if (pkList.contains(courierGroupHasCourier.getCourierGroupName()))
//                throw new RuntimeException("Duplicate key " + courierGroupHasCourier.name());
//            else pkList.add(courierGroupHasCourier.name());
//
//            insert(courierGroupHasCourier.name(), courierGroupHasCourier.getEnumCouriers());
//        }
//    }
//
//
//}
