package com.hk.admin.impl.service.shippingOrder;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.domain.courier.Shipment;
import com.hk.pact.dao.BaseDao;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private BaseDao baseDao;

    public Shipment saveShipmentDate(Shipment shipment) {
        shipment.setShipDate(new Date());
        return (Shipment) getBaseDao().save(shipment);
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) getBaseDao().save(shipment);
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

   

}
