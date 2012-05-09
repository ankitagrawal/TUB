package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.courier.Courier;
import com.hk.pact.dao.BaseDao;

public interface CourierDao extends BaseDao {

    public Courier getCourierByName(String name);

    public Courier getCourierById(Long courierId);

    public List<Courier> getAllCouriers();

    public List<Courier> listRestOfIndiaAvailableCouriers();

    public Courier getPreferredCourierForState(String state);

}
