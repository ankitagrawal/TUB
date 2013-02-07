package com.hk.admin.pact.service.courier;

import com.akube.framework.dao.Page;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;

public interface CourierService {

    public List<Courier> getAllCouriers();

    public Courier getCourierById(Long courierId);

    public Courier getCourierByName(String name);

    public Courier save(Courier courier);

    public List<Courier> getCouriers(List<Long> courierIds, List<String> courierNames, Boolean active, Long operationBitset);

    public Page getCouriers(String courierName, Boolean active, String courierGroup, int page, int perPage, Long operationsBitset);

    public void saveOrUpdate(Courier courier);
}
