package com.hk.admin.pact.dao.courier;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface AwbDao extends BaseDao {

    public List<Awb> getAvailableAwbForCourierByWarehouseAndCod(Courier courier, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

     public Awb findByCourierWarehouseCodAwbnumber(Courier courier, Warehouse warehouse,Boolean cod ,String awbNumber);

      public List<Awb> getUsedAwb(String AwbNumber);

  public  List<Awb> getAwbByAwbNumberAndStatus(String awbNumber,AwbStatus awbStatus);
     
}
