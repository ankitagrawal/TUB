package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.constants.courier.EnumAwbStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 10:56:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AwbServiceImpl implements AwbService {
    @Autowired
    AwbDao awbDao;

    public Awb find(Long id) {
        return awbDao.get(Awb.class, id);
    }


    public List<Awb> getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus) {
        return awbDao.getAvailableAwbForCourierByWarehouseCodStatus(courier, awbNumber, warehouse, cod, awbStatus);
    }


    public Awb save(Awb awb) {
        return (Awb) awbDao.save(awb);
    }

}
