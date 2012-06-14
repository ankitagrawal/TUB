package com.hk.admin.pact.dao.courier;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface AwbDao extends BaseDao {

//    public List<Awb> getAvailableAwbForCourier(Courier courier, Warehouse warehouse);

    public Awb getByAWBNumber(Courier courier, String awbNumber);

   public List<Awb> getAvailableAwbForCourierByWarehouseAndCod(Courier courier, Warehouse warehouse,Boolean cod);

}
