package com.hk.admin.impl.service.courier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jul 13, 2012
 * Time: 10:56:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AwbServiceImpl implements AwbService {
    @Autowired
    AwbDao awbDao;
    @Autowired
    ShipmentService  shipmentService;

    public Awb find(Long id) {
        return awbDao.get(Awb.class, id);
    }


    public List<Awb> getAvailableAwbListForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus) {
        return awbDao.getAvailableAwbForCourierByWarehouseCodStatus(courier, awbNumber, warehouse, cod, awbStatus);
    }

    public Awb getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus) {
        List<Awb> awbList = awbDao.getAvailableAwbForCourierByWarehouseCodStatus(courier, awbNumber, warehouse, cod, awbStatus);
        return awbList != null && !awbList.isEmpty() ? awbList.get(0) : null;
    }

    public Awb save(Awb awb) {
        return (Awb) awbDao.save(awb);
    }

    public Awb findByCourierAwbNumber(Courier courier ,String awbNumber){
        return awbDao.findByCourierAwbNumber(courier ,awbNumber);
    }

    public List<Awb> getAllAwb() {
        return awbDao.getAll(Awb.class);
    }

    public List<Awb> getAlreadyPresentAwb(Courier courier,List<String> awbNumberList) {
        return awbDao.getAlreadyPresentAwb(courier,awbNumberList);
    }
}
