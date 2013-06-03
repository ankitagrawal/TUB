package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface AwbDao extends BaseDao {

    public List<Awb> getAvailableAwbForCourierByWarehouseCodStatus(List<Courier> couriers, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

    public List<Awb> getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

    public Awb findByCourierAwbNumber(Courier courier, String awbNumber);

    public Awb findByCourierAwbNumber(List<Courier> couriers, String awbNumber);

    public List<Awb> getAlreadyPresentAwb(Courier courier, List<String> awbNumberList);

    public Awb save(Awb awb, Integer newStatus);

    public Awb isAwbEligibleForDeletion(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod);

}
