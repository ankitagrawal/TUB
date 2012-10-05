package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.courier.Courier;
import com.hk.pact.dao.BaseDao;

public interface CourierDao extends BaseDao {

    public Courier getCourierByName(String name);

    public List<Courier> getCourierByIds(List<Long> courierId);

	public List<Courier> getAvailableCouriers();

    public Courier getPreferredCourierForState(String state); 

   public List<Courier> getDisableCourier();




}
