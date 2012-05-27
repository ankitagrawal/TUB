package com.hk.admin.pact.service.courier;

import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/25/12
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourierGroupService {

    public Set<Courier> getCommonCouriers(CourierGroup courierGroup, List<Courier> courierList);

    public CourierGroup getCourierGroup(Courier courier);


    }
