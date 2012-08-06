package com.hk.admin.pact.dao.courier;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface AwbDao extends BaseDao {

    public List<Awb> getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

     public Awb findByCourierAwbNumber(Courier courier ,String awbNumber);

     public List<Awb> getAlreadyPresentAwb(Courier courier,List<String> awbNumberList);

}
