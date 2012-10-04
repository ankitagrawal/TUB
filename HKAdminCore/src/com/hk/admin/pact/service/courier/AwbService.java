package com.hk.admin.pact.service.courier;

import java.util.List;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
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

    public List<Awb> getAllAwb();

    public List<Awb> getAlreadyPresentAwb(Courier courier,List<String> awbNumberList);

    public Awb createAwb(Courier suggestedCourier, String trackingNumber, Warehouse warehouse, Boolean isCod);


}
