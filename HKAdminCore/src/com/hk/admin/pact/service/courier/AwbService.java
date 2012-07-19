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

    public Awb findByCourierAwbNumber(Courier courier ,String awbNumber);

    public List<Awb> getAvailableAwbListForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

    public Awb getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

    public Awb save(Awb awb);


}
