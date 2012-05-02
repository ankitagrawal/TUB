package com.hk.admin.impl.service.shippingOrder;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.impl.dao.courier.ShipmentDao;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.domain.courier.Shipment;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private ShipmentDao shipmentDao;

    public Shipment saveShipmentDate(Shipment shipment) {
        shipment.setShipDate(new Date());
        return (Shipment) getShipmentDao().save(shipment);
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) getShipmentDao().save(shipment);
    }

    public ShipmentDao getShipmentDao() {
        return shipmentDao;
    }

    public void setShipmentDao(ShipmentDao shipmentDao) {
        this.shipmentDao = shipmentDao;
    }

}
