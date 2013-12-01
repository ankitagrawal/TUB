package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.courier.Courier;
import com.hk.pact.dao.BaseDao;
import com.akube.framework.dao.Page;

public interface CourierDao extends BaseDao {

    public List<Courier> getCourierByIds(List<Long> courierId);

    public Courier getPreferredCourierForState(String state);

	public List<Courier> getCouriers(List<Long> courierIds, List<String> courierNames, Boolean active, Long operationBitset);

	public Page getCouriers(String courierName, Boolean active, String courierGroup, int page, int perPage, Long operationBitset);

}
