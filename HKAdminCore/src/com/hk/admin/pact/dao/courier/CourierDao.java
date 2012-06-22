package com.hk.admin.pact.dao.courier;

import com.hk.domain.courier.Courier;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface CourierDao extends BaseDao {

    public Courier getCourierByName(String name);

    public List<Courier> getCourierByIds(List<Long> courierId);

    public List<Courier> getAllCouriers();

    public Courier getPreferredCourierForState(String state);

}
