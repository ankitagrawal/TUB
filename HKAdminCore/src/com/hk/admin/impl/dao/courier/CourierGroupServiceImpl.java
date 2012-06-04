package com.hk.admin.impl.dao.courier;

import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.impl.dao.BaseDaoImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/25/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CourierGroupServiceImpl extends BaseDaoImpl implements CourierGroupService {

    public CourierGroup getByName(String name){
        return (CourierGroup) getSession().createQuery("select c from CourierGroup c where c.name = :name")
                .setParameter("name", name)
                .uniqueResult();
    }

    public Set<Courier> getCommonCouriers(CourierGroup courierGroup, List<Courier> courierList){
        Set<Courier> commonCouriers = new HashSet<Courier>();
        if(courierList.isEmpty()) return commonCouriers;
        Set<Courier> applicableCourierHashSet = new HashSet<Courier>();
        applicableCourierHashSet.addAll(courierList);
        for (Courier courier : courierGroup.getCouriers()) {
            if(applicableCourierHashSet.contains(courier)){
                commonCouriers.add(courier);
            }
        }
        return commonCouriers;
    }

    public CourierGroup getCourierGroup(Courier courier) {
        for (CourierGroup courierGroup : getAll(CourierGroup.class)) {
            for (Courier subsetCourier : courierGroup.getCouriers()) {
                if (subsetCourier.equals(courier)) {
                    return courierGroup;
                }
            }
        }
        return null;
    }

}
