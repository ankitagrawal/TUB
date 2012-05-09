package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface AwbDao extends BaseDao {

    public List<Awb> getAvailableAwbForCourier(Courier courier, Warehouse warehouse);

    public Awb getByAWBNumber(Courier courier, String awbNumber);
}
