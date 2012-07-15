package com.hk.admin.pact.service.courier;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 10:55:34 AM
 * To change this template use File | Settings | File Templates.
 */

public interface AwbService {

    public Awb find(Long id);

    public List<Awb>  getUnusedAwbForCourierByWarehouseAndCod(Courier courier, Warehouse warehouse, Boolean cod);

    public Awb findByCourierWarehouseCodAwbnumber(Courier courier, Warehouse warehouse, Boolean cod, String awbNumber);

     public  List<Awb> getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

    public List<Awb> getUsedAwb(String AwbNumber);

    public Awb save(Awb awb);

   public  List<Awb> getAwbByAwbNumberAndStatus(String AwbNumber,AwbStatus awbStatus);

}
