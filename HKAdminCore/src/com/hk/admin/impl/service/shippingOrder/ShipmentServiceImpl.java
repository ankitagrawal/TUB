package com.hk.admin.impl.service.shippingOrder;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.impl.dao.BaseDaoImpl;


@Service
public class ShipmentServiceImpl  implements ShipmentService {

    @Autowired
    private BaseDao baseDao;
    @Autowired
    AwbService awbService;
    @Autowired
    ShipmentDao shipmentDao;


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

    public Awb attachAwbToShipment(Courier courier, ShippingOrder shippingOrder) {
        Shipment shipment = shippingOrder.getShipment();
        List<Awb> suggestedAwbList = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), shippingOrder.isCOD(), EnumAwbStatus.Unused.getAsAwbStatus());
        if (suggestedAwbList != null && suggestedAwbList.size() > 0) {
            Awb suggestedAwb = suggestedAwbList.get(0);
            AwbStatus awbStatus = EnumAwbStatus.Attach.getAsAwbStatus();
            suggestedAwb.setAwbStatus(awbStatus);
            awbService.save(suggestedAwb);
            shipment.setAwb(suggestedAwb);
            return suggestedAwb;
        }
        return null;
    }

  public Shipment findByAwb(Awb awb){
      return shipmentDao.findByAwb(awb);
  }
}
